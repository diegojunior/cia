package br.com.totvs.cia.cadastro.base.model;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum CondicaoEnum implements PersistentEnum {

	COMECA_COM("Começa Com", "CC"), 
	
	CONTEM("Contém", "CO"),
	
	DIFERENTE("Diferente", "DI"), 
	
	IGUAL("Igual", "IG");

	private final String nome;
	
	private final String codigo;

	private CondicaoEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;

	}

	public static CondicaoEnum fromCodigo(final String codigo) {
		for (CondicaoEnum tipo : CondicaoEnum.values()) {
			if (tipo.getCodigo().equalsIgnoreCase(codigo))
				return tipo;
		}
		return null;
	}

	public Boolean isIgual() {
		return this == IGUAL;
	}
	
	public Boolean isContem() {
		return this == CONTEM;
	}
	
	public Boolean isDiferente() {
		return this == DIFERENTE;
	}
	
	public Boolean isComecaCom() {
		return this == COMECA_COM;
	}
}
