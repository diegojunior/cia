package br.com.totvs.cia.importacao.job;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.FixedLengthTokenizer;
import org.springframework.batch.item.file.transform.IncorrectLineLengthException;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import br.com.totvs.cia.importacao.exception.ImportacaoArquivoParseException;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;

@Component
public class ImportacaoItemReader extends FlatFileItemReader<UnidadeImportacaoProcessamentoJson> {

	private static final Logger LOG = Logger.getLogger(ImportacaoItemReader.class);

	private static final String SUFIXO = "*";

	private final PatternMatchingCompositeLineMapper<UnidadeImportacaoProcessamentoJson> lineMapper;

	@Autowired
	private ImportacaoService importacaoService;

	public ImportacaoItemReader() {
		this.lineMapper = new PatternMatchingCompositeLineMapper<UnidadeImportacaoProcessamentoJson>();
		this.setLineMapper(this.lineMapper);
	}

	public ItemReader<UnidadeImportacaoProcessamentoJson> reader(final String importacaoId) {
		final Map<String, FieldSetMapper<UnidadeImportacaoProcessamentoJson>> fieldSetMappers = Maps.newHashMap();
		final Map<String, LineTokenizer> tokenizers = Maps.newHashMap();
		final Importacao importacao = this.importacaoService.findOne(importacaoId);
		final Resource resource = new ByteArrayResource(importacao.getArquivo().getDadosArquivo().getDados());

		this.configurarLineMapper(fieldSetMappers, tokenizers, importacao);
		this.adicionarMapperDefault(fieldSetMappers);
		this.adicionarDefaultTokenizer(tokenizers);
		this.lineMapper.setFieldSetMappers(fieldSetMappers);
		this.lineMapper.setTokenizers(tokenizers);
		this.setResource(resource);
		this.open(new ExecutionContext());
		return this;

	}

	@Override
	protected UnidadeImportacaoProcessamentoJson doRead() throws Exception {
		try {

			return super.doRead();

		} catch (final Exception e) {
			LOG.debug(e.getMessage());
			final StringBuilder msg = new StringBuilder();
			if (e instanceof FlatFileParseException){
				final FlatFileParseException flatFileException = ((FlatFileParseException)e);
				msg.append("Houve um erro na linha: ").append(flatFileException.getLineNumber()).append(".");
				if (e.getCause() instanceof IncorrectLineLengthException) {
					msg.append(" O tamanho da linha está incorreto.");
				}
			} else {
				msg.append(
						"Ocorreu um erro interno ao executar o job de importação. Favor verificar o log e acionar o suporte. ");
			}
			throw new ImportacaoArquivoParseException(msg.toString());
		}
	}

	private void configurarLineMapper(
			final Map<String, FieldSetMapper<UnidadeImportacaoProcessamentoJson>> fieldSetMappers,
			final Map<String, LineTokenizer> tokenizers, final Importacao importacao) {

		for (final Sessao sessao : importacao.getLayout().getSessoes()) {
			final JsonObjectFieldSetMapper jsonFieldSetMapper = new JsonObjectFieldSetMapper(sessao);
			final FixedLengthTokenizer fixedLineTokenizer = new FixedLengthTokenizer();
			final String codigo = sessao.getCodigo() + SUFIXO;
			final List<Range> ranges = Lists.newArrayList();
			for (final Campo campo : sessao.getCampos()) {
				final Range range = new Range(campo.getPosicaoInicial(), campo.getPosicaoFinal());
				ranges.add(range);
			}
			fixedLineTokenizer.setColumns(ranges.toArray(new Range[] {}));
			fieldSetMappers.put(codigo, jsonFieldSetMapper);
			tokenizers.put(codigo, fixedLineTokenizer);
		}
	}

	private void adicionarDefaultTokenizer(final Map<String, LineTokenizer> tokenizers) {
		final FixedLengthTokenizer fixedLineTokenizer = new FixedLengthTokenizer();
		fixedLineTokenizer.setStrict(false);
		fixedLineTokenizer.setColumns(new Range[] {});
		tokenizers.put(SUFIXO, fixedLineTokenizer);
	}

	private void adicionarMapperDefault(
			final Map<String, FieldSetMapper<UnidadeImportacaoProcessamentoJson>> fieldSetMappers) {
		fieldSetMappers.put(SUFIXO, new DefaultObjectFieldSetMapper());
	}

}
