package br.com.totvs.cia.conciliacao.model;

import lombok.Getter;
import br.com.totvs.cia.infra.enums.PersistentEnum;

@Getter
public enum StatusCampoConciliacaoEnum implements PersistentEnum {

	OK ("OK", "OK"),

	DIVERGENTE("Divergente", "DI"),
	
	SOMENTE_CARGA("Somente Carga", "SC"),
	
	SOMENTE_IMPORTACAO("Somente Importação", "SI");

	private final String nome;

	private final String codigo;

	private StatusCampoConciliacaoEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusCampoConciliacaoEnum fromCodigo(final String codigo) {
		for (StatusCampoConciliacaoEnum status : StatusCampoConciliacaoEnum.values()) {
			if (codigo.equalsIgnoreCase(status.getCodigo()))
				return status;
		}
		return null;
	}
}
