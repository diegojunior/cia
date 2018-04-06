package br.com.totvs.cia.importacao.job;

import java.util.Collections;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStream;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.mapping.PatternMatchingCompositeLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import br.com.totvs.cia.importacao.exception.ImportacaoArquivoParseException;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.parametrizacao.layout.model.CampoDelimitadorComparator;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoDelimitadorEnum;
@Component
public class ImportacaoItemDelimitadorReader implements ItemReader<UnidadeImportacaoProcessamentoJson>, ItemStream {
	
	private static final Logger LOG = Logger.getLogger(ImportacaoItemDelimitadorReader.class);
	
	private static final String SUFIXO = "*";

	private FlatFileItemReader<UnidadeImportacaoProcessamentoJson> delegate;
	
	@Autowired
	private ImportacaoService importacaoService;

	public ImportacaoItemDelimitadorReader loadReader(final String importacaoId) {
		Importacao importacao = this.importacaoService.findOne(importacaoId);
		Map<String, FieldSetMapper<UnidadeImportacaoProcessamentoJson>> fieldSetMappers = Maps.newHashMap();
		Map<String, LineTokenizer> tokenizers = Maps.newHashMap();
		CustomPatternMatchingCompositeLineMapper customPatternLineMapper = new CustomPatternMatchingCompositeLineMapper(new PatternMatchingCompositeLineMapper<UnidadeImportacaoProcessamentoJson>());
		Resource resource = new ByteArrayResource(importacao.getArquivo().getDadosArquivo().getDados());

		this.adicionarMapperDefault(fieldSetMappers);
		this.adicionarDefaultTokenizer(tokenizers, importacao.getLayout().getTipoDelimitador());
		this.configureLineMapper(importacao, fieldSetMappers, tokenizers, customPatternLineMapper);
		this.delegate = new FlatFileItemReader<UnidadeImportacaoProcessamentoJson>();
		this.delegate.setResource(resource);
		this.delegate.setLineMapper(customPatternLineMapper);
		this.open(new ExecutionContext());
		return this;
	}

	@Override
	public UnidadeImportacaoProcessamentoJson read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		try {
			return this.delegate.read();
		} catch (Exception e) {
			LOG.debug(e.getMessage());
			final StringBuilder msg = new StringBuilder();
			if (e instanceof FlatFileParseException){
				final FlatFileParseException flatFileException = ((FlatFileParseException)e);
				msg.append("Houve um erro na linha: ").append(flatFileException.getLineNumber()).append(". ");
				if (e.getCause() instanceof ImportacaoArquivoParseException) {
					msg.append(e.getCause().getMessage());
				}
			} else {
				msg.append(
						"Ocorreu um erro interno ao executar o job de importação. Favor verificar o log e acionar o suporte. ");
			}
			throw new ImportacaoArquivoParseException(msg.toString());
		}
	}

	@Override
	public void open(final ExecutionContext executionContext) throws ItemStreamException {

		this.delegate.open(executionContext);
		
	}
	
	@Override
	public void update(final ExecutionContext executionContext) throws ItemStreamException {
		this.delegate.update(executionContext);
	}

	@Override
	public void close() throws ItemStreamException {
		this.delegate.close();
	}
	
	private void configureLineMapper(final Importacao importacao,
			final Map<String, FieldSetMapper<UnidadeImportacaoProcessamentoJson>> fieldSetMappers,
			final Map<String, LineTokenizer> tokenizers, final CustomPatternMatchingCompositeLineMapper customPatternLineMapper) {
		for (Sessao sessao : importacao.getLayout().getSessoes()) {
			Collections.sort(sessao.getCampos(), new CampoDelimitadorComparator());
			DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer(importacao.getLayout().getTipoDelimitador().getValorTipoDelimitador());
			String codigo = "SEM SESSÂO".equalsIgnoreCase(sessao.getCodigo()) ? SUFIXO : sessao.getCodigo() + SUFIXO;
			fieldSetMappers.put(codigo, new JsonObjectFieldSetMapper(sessao));
			tokenizers.put(codigo, tokenizer);
		}
		customPatternLineMapper.setFieldSetMappers(fieldSetMappers);
		customPatternLineMapper.setTokenizers(tokenizers);
	}

	private void adicionarMapperDefault(
			final Map<String, FieldSetMapper<UnidadeImportacaoProcessamentoJson>> fieldSetMappers) {
		fieldSetMappers.put(SUFIXO, new DefaultObjectFieldSetMapper());
	}
	
	private void adicionarDefaultTokenizer(final Map<String, LineTokenizer> tokenizers, final TipoDelimitadorEnum tipoDelimitadorEnum) {
		final DelimitedLineTokenizer defaultLineToken = new DelimitedLineTokenizer(tipoDelimitadorEnum.getValorTipoDelimitador());
		defaultLineToken.setStrict(false);
		tokenizers.put(SUFIXO, defaultLineToken);
	}

}
