package br.com.totvs.cia.carga.json;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum StatusLoteCargaJsonEnum implements PersistentEnum {
	
	ANDAMENTO ("Em Andamento", "AN"),

	ERRO("Erro", "ER"),

	CONCLUIDO("Concluído", "CO");

	private final String nome;

	private final String codigo;

	private StatusLoteCargaJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusLoteCargaJsonEnum fromCodigo(final String codigo) {
		for (StatusLoteCargaJsonEnum status : StatusLoteCargaJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(status.getCodigo()))
				return status;
		}
		return null;
	}
}
