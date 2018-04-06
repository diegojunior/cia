package br.com.totvs.cia.conciliacao.model;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum StatusLoteConciliacaoEnum implements PersistentEnum {

	OK ("OK", "OK"),

	DIVERGENTE("Divergente", "DI"),
	
	ERRO("Erro", "ER");

	private final String nome;

	private final String codigo;

	private StatusLoteConciliacaoEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusLoteConciliacaoEnum fromCodigo(final String codigo) {
		for (StatusLoteConciliacaoEnum status : StatusLoteConciliacaoEnum.values()) {
			if (codigo.equalsIgnoreCase(status.getCodigo()))
				return status;
		}
		return null;
	}

	public Boolean isDivergente() {
		return this == DIVERGENTE;
	}
	
	public Boolean isErro() {
		return this == StatusLoteConciliacaoEnum.ERRO;
	}
	
	public Boolean isOk() {
		return this == StatusLoteConciliacaoEnum.OK;
	}
	
}
