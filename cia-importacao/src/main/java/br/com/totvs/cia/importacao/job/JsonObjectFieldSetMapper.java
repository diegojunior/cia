package br.com.totvs.cia.importacao.job;

import java.util.Iterator;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import br.com.totvs.cia.importacao.exception.ImportacaoArquivoParseException;
import br.com.totvs.cia.importacao.json.CampoImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;

public class JsonObjectFieldSetMapper implements FieldSetMapper<UnidadeImportacaoProcessamentoJson> {

	private final Sessao sessao;

	public JsonObjectFieldSetMapper(final Sessao sessao) {
		this.sessao = sessao;
	}

	@Override
	public UnidadeImportacaoProcessamentoJson mapFieldSet(final FieldSet fs) throws BindException {
		final UnidadeImportacaoProcessamentoJson unidadeImportacaoProcessamentoJson = new UnidadeImportacaoProcessamentoJson();
		unidadeImportacaoProcessamentoJson.setCodigoSessao(this.sessao.getCodigo());
		unidadeImportacaoProcessamentoJson.setIdSessao(this.sessao.getId());
		if (fs != null) {
			int quantidadeCamposSessao = this.sessao.getCampos().size();
			int quantidadeFieldsImportados = fs.getFieldCount();
			if (quantidadeCamposSessao > quantidadeFieldsImportados
					|| quantidadeCamposSessao < quantidadeFieldsImportados) {
				throw new ImportacaoArquivoParseException("A quantidade de campos no arquivo importado esta divergente com os campos configurados no layout.");
			}
			final Iterator<Campo> iterator = this.sessao.getCampos().iterator();
			for (final String valor : fs.getValues()) {
				final Campo campo = iterator.next();
				unidadeImportacaoProcessamentoJson
						.add(new CampoImportacaoProcessamentoJson(campo.getDominio().getCodigo(), valor, campo.getPattern()));
			}
		}
		return unidadeImportacaoProcessamentoJson;
	}

}