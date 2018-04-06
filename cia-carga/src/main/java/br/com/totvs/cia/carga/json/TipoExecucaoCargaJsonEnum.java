package br.com.totvs.cia.carga.json;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum TipoExecucaoCargaJsonEnum implements PersistentEnum {
	
	CLIENTE ("Cliente", "CL"),

	GRUPO ("Grupo", "GR"),

	TODOS("Todos", "TO");

	private final String nome;

	private final String codigo;

	private TipoExecucaoCargaJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static TipoExecucaoCargaJsonEnum fromCodigo(final String codigo) {
		for (TipoExecucaoCargaJsonEnum status : TipoExecucaoCargaJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(status.getCodigo()))
				return status;
		}
		return null;
	}
	
	public Boolean isCliente () {
		return this == CLIENTE;
	}
	
	public Boolean isGrupo () {
		return this == GRUPO;
	}
	
	public Boolean isTodos () {
		return this == TODOS;
	}
}