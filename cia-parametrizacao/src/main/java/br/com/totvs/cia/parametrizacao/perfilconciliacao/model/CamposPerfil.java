package br.com.totvs.cia.parametrizacao.perfilconciliacao.model;

import java.util.Iterator;
import java.util.List;

import lombok.Getter;

public class CamposPerfil implements Iterable<CampoPerfilConciliacao> {
	
	@Getter
	private List<CampoPerfilConciliacao> campos;
	
	public CamposPerfil(final List<CampoPerfilConciliacao> campos, final ConfiguracaoPerfilConciliacao configuracao) {
		configuracao.getCampos().clear();
		for (CampoPerfilConciliacao campo : campos) {
			campo.setConfiguracaoPerfil(configuracao);
		}
		configuracao.setCampos(campos);
		this.campos = campos;
	}
	
	@Override
	public Iterator<CampoPerfilConciliacao> iterator() {
		return this.campos.iterator();
	}
	
	public CampoPerfilConciliacao get(final Integer index) {
		return this.campos.get(index);
	}

	public Integer size() {
		return this.campos.size();
	}
}