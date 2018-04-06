package br.com.totvs.cia.importacao.job;

import java.util.Iterator;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.google.common.base.Strings;

import br.com.totvs.cia.importacao.json.CampoImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;

public class FragmentFieldSetMapper implements FieldSetMapper<UnidadeImportacaoProcessamentoJson> {

	private final Sessao sessao;

	public FragmentFieldSetMapper(final Sessao sessao) {
		this.sessao = sessao;
	}

	@Override
	public UnidadeImportacaoProcessamentoJson mapFieldSet(final FieldSet fs) throws BindException {
		final UnidadeImportacaoProcessamentoJson unidadeImportacaoProcessamentoJson = new UnidadeImportacaoProcessamentoJson();
		unidadeImportacaoProcessamentoJson.setCodigoSessao(this.sessao.getCodigo());
		unidadeImportacaoProcessamentoJson.setIdSessao(this.sessao.getId());
		if (fs != null) {
			final Iterator<Campo> campos = this.sessao.getCampos().iterator();
			for (final String value : fs.getValues()) {
				if (campos.hasNext()) {
					final Campo campo = campos.next();
					String newValue = value;
					if (campo.getDominio().isEfetuaCalculo()) {
						newValue = this.aplicaFormatacao(value, campo.getPattern());
					}
					final CampoImportacaoProcessamentoJson campoParametrizacao = new CampoImportacaoProcessamentoJson(campo.getDominio().getCodigo(), newValue, campo.getPattern());
					unidadeImportacaoProcessamentoJson.add(campoParametrizacao);
				}
			}
		}
		return unidadeImportacaoProcessamentoJson;
	}
	
	public String aplicaFormatacao(final String oldValue, final String pattern) {
		String regex = "[.|,]";
		String[] gruposFormatos = pattern.split(regex);
		int qtdGruposFormatos = gruposFormatos.length;
		if (oldValue.split(regex).length > 1) {
			return oldValue;
		}
		if (this.decimal(qtdGruposFormatos)) {
			int qtdDecimais = gruposFormatos[--qtdGruposFormatos].length();
			return Strings.padEnd(oldValue, oldValue.length() + qtdDecimais, '0');
		}
		return oldValue;
	}

	private boolean decimal(final int qtdGruposFormatos) {
		return qtdGruposFormatos > 1;
	}
	
}