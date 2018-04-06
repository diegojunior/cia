package br.com.totvs.cia.parametrizacao.perfilconciliacao.json;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

public enum StatusPerfilJsonEnum implements PersistentEnum {

	ATIVO("Ativo", "ATV"), 
	INATIVO("Inativo", "ITV");
	
	@Getter
	private final String nome;
	@Getter
	private final String codigo;

	private StatusPerfilJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static StatusPerfilJsonEnum fromCodigo(final String codigo) {
		for (StatusPerfilJsonEnum tipo : StatusPerfilJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}
	
}
