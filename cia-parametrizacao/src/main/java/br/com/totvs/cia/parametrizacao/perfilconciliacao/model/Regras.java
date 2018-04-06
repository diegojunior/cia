package br.com.totvs.cia.parametrizacao.perfilconciliacao.model;

import java.util.Iterator;
import java.util.List;

import lombok.Getter;

public class Regras implements Iterable<Regra> {
	
	@Getter
	private List<Regra> regras;
	
	public Regras(final List<Regra> regras, final PerfilConciliacao perfil) {
		perfil.getRegras().clear();
		for (Regra regra : regras) {
			regra.setPerfil(perfil);
		}
		perfil.setRegras(regras);
		this.regras = regras;
	}
	
	@Override
	public Iterator<Regra> iterator() {
		return this.regras.iterator();
	}
	
	public Regra get(final Integer index) {
		return this.regras.get(index);
	}

	public Integer size() {
		return this.regras.size();
	}
}