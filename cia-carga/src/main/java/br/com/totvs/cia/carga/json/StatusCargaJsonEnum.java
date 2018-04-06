package br.com.totvs.cia.carga.json;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum StatusCargaJsonEnum implements PersistentEnum {
	
	ANDAMENTO ("Em Andamento", "AN"),

	ERRO("Erro", "ER"),

	CONCLUIDO("Concluído", "CO"),
	
	NAO_EXECUTADO("Não Executado", "NE");

	private final String nome;

	private final String codigo;

	private StatusCargaJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusCargaJsonEnum fromCodigo(final String codigo) {
		for (StatusCargaJsonEnum status : StatusCargaJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(status.getCodigo()))
				return status;
		}
		return null;
	}
}
