package br.com.totvs.cia.conciliacao.json;

import br.com.totvs.cia.infra.json.Json;
import lombok.Getter;

@Getter
public enum StatusUnidadeConciliacaoJsonEnum implements Json {
	
	OK ("OK", "OK"),

	DIVERGENTE("Divergente", "DI"),
	
	CHAVE_NAO_IDENTIFICADA("Chave não Identificada", "CN");
	
	private final String nome;

	private final String codigo;

	private StatusUnidadeConciliacaoJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusUnidadeConciliacaoJsonEnum fromCodigo(final String codigo) {
		for (StatusUnidadeConciliacaoJsonEnum tipo : StatusUnidadeConciliacaoJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	} 

}
