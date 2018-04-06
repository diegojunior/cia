package br.com.totvs.cia.parametrizacao.perfilconciliacao.model;

import lombok.Getter;
import br.com.totvs.cia.infra.enums.PersistentEnum;

public enum StatusPerfilEnum implements PersistentEnum {

	ATIVO("Ativo", "ATV"), 
	INATIVO("Inativo", "ITV");
	
	@Getter
	private final String nome;
	@Getter
	private final String codigo;

	private StatusPerfilEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
		
	}
	
	public static StatusPerfilEnum fromCodigo(final String codigo) {
		for (StatusPerfilEnum tipo : StatusPerfilEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}
	
}
