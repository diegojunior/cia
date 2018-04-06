package br.com.totvs.cia.conciliacao.service;

import com.google.common.collect.Lists;

import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.Regra;

public abstract class AbstractRegraConciliacao {
	
	private static final String DELIMITADOR = ",";
	
	protected Boolean isEnquadrado(final Regra regra, final String valor) {
		
		if (valor == null) {
			return false;
		}
		
		if (regra.getCondicao().isIgual()) {
			return this.IsIgual(regra, valor);
		}
		
		if (regra.getCondicao().isDiferente()) {
			return this.isDiferente(regra, valor);
		}
		
		if (regra.getCondicao().isContem()) {
			return this.isContem(regra, valor);
		}
		
		if (regra.getCondicao().isComecaCom()) {
			return this.isComecaCom(regra, valor);
		}
		
		throw new RuntimeException("Condição de Regra de Conciliação não identificada");
	}

	protected Boolean IsIgual(final Regra regra, final String valor) {
		return Lists.newArrayList(regra.getFiltro().split(DELIMITADOR))
				 .stream()
				 .filter(filtro -> valor.trim().equalsIgnoreCase(filtro.trim()))
				 .findFirst()
				 .isPresent();
	}
	
	protected Boolean isDiferente(final Regra regra, final String valor) {
		for (String filtro : regra.getFiltro().split(DELIMITADOR)) {
			Boolean isIgual = valor.trim().equalsIgnoreCase(filtro.trim());
			if (isIgual) {
				return false;
			}
		}
		return true;
	}
	
	protected Boolean isContem(final Regra regra, final String valor) {
		return Lists.newArrayList(regra.getFiltro().split(DELIMITADOR))
				 .stream()
				 .filter(filtro -> valor.trim().toUpperCase().contains(filtro.trim().toUpperCase()))
				 .findFirst()
				 .isPresent();
	}

	protected Boolean isComecaCom(final Regra regra, final String valor) {
		return Lists.newArrayList(regra.getFiltro().split(DELIMITADOR))
				 .stream()
				 .filter(filtro -> valor.trim().toUpperCase().startsWith(filtro.trim().toUpperCase()))
				 .findFirst()
				 .isPresent();
	}
}
