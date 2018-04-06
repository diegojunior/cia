package br.com.totvs.cia.carga.model;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum StatusLoteCargaEnum implements PersistentEnum{

	ANDAMENTO ("Em Andamento", "AN"),

	ERRO("Erro", "ER"),

	CONCLUIDO("Concluído", "CO");

	private final String nome;

	private final String codigo;

	private StatusLoteCargaEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static StatusLoteCargaEnum fromCodigo(final String codigo) {
		for (final StatusLoteCargaEnum status : StatusLoteCargaEnum.values()) {
			if (codigo.equalsIgnoreCase(status.getCodigo()))
				return status;
		}
		return null;
	}

	public boolean isAndamento() {
		return this == ANDAMENTO;
	}
}