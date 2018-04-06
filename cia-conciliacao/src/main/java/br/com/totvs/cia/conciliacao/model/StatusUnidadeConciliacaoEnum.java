package br.com.totvs.cia.conciliacao.model;

import lombok.Getter;
import br.com.totvs.cia.infra.enums.PersistentEnum;

@Getter
public enum StatusUnidadeConciliacaoEnum implements PersistentEnum {

	OK ("OK", "OK"),

	DIVERGENTE("Divergente", "DI"),
	
	CHAVE_NAO_IDENTIFICADA("Chave não Identificada", "CN");
	
	private final String nome;

	private final String codigo;

	private StatusUnidadeConciliacaoEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusUnidadeConciliacaoEnum fromCodigo(final String codigo) {
		for (StatusUnidadeConciliacaoEnum status : StatusUnidadeConciliacaoEnum.values()) {
			if (codigo.equalsIgnoreCase(status.getCodigo()))
				return status;
		}
		return null;
	}
}
