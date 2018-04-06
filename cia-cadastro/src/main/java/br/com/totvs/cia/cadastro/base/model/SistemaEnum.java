package br.com.totvs.cia.cadastro.base.model;

import lombok.Getter;
import br.com.totvs.cia.infra.enums.PersistentEnum;

@Getter
public enum SistemaEnum implements PersistentEnum {

	AMPLIS("AMPLIS", "AMP"), JCOT("JCOT", "JCT");

	private final String nome;
	
	private final String codigo;

	private SistemaEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;

	}

	public static SistemaEnum fromCodigo(final String codigo) {
		for (SistemaEnum tipo : SistemaEnum.values()) {
			if (tipo.getCodigo().equalsIgnoreCase(codigo))
				return tipo;
		}
		return null;
	}
	
	public boolean isAmplis(){
		return this == AMPLIS;
	}
	
	public boolean isJcot(){
		return this == JCOT;
	}
}
