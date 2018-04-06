package br.com.totvs.cia.conciliacao.model;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum StatusConciliacaoEnum implements PersistentEnum {

	ANDAMENTO ("Em Andamento", "AN"),

	DIVERGENTE("Divergente", "DI"),
	
	OK("OK", "OK"),
	
	ERRO("Erro", "ER"),

	GRAVADA("Gravada", "GR"),
	
	GRAVADA_DIVERGENCIA("Gravada com Divergências", "GD");

	private final String nome;

	private final String codigo;

	private StatusConciliacaoEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusConciliacaoEnum fromCodigo(final String codigo) {
		for (StatusConciliacaoEnum status : StatusConciliacaoEnum.values()) {
			if (codigo.equalsIgnoreCase(status.getCodigo()))
				return status;
		}
		return null;
	}

	public Boolean isDivergente() {
		return this == DIVERGENTE;
	}
	
	public Boolean isErro() {
		return this == ERRO;
	}
	
	public Boolean isGravada() {
		return this == GRAVADA || this == GRAVADA_DIVERGENCIA;
	}
}
