package br.com.totvs.cia.carga.model;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum StatusCargaEnum implements PersistentEnum{

	ANDAMENTO ("Em Andamento", "AN"),

	ERRO("Erro", "ER"),

	CONCLUIDO("Concluído", "CO"),
	
	NAO_EXECUTADO("Não Executado", "NE");

	private final String nome;

	private final String codigo;

	private StatusCargaEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static StatusCargaEnum fromCodigo(final String codigo) {
		for (final StatusCargaEnum status : StatusCargaEnum.values()) {
			if (codigo.equalsIgnoreCase(status.getCodigo()))
				return status;
		}
		return null;
	}
}