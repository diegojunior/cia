package br.com.totvs.cia.conciliacao.json;

import lombok.Getter;
import br.com.totvs.cia.infra.json.Json;

@Getter
public enum StatusCampoConciliacaoJsonEnum implements Json {
	
	OK ("OK", "OK"),

	DIVERGENTE("Divergente", "DI"),
	
	SOMENTE_CARGA("Somente Carga", "SC"),
	
	SOMENTE_IMPORTACAO("Somente Importação", "SI");
	
	private final String nome;
	
	private final String codigo;

	private StatusCampoConciliacaoJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusCampoConciliacaoJsonEnum fromCodigo(final String codigo) {
		for (StatusCampoConciliacaoJsonEnum tipo : StatusCampoConciliacaoJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	} 
}