package br.com.totvs.cia.conciliacao.json;

import br.com.totvs.cia.infra.json.Json;
import lombok.Getter;

@Getter
public enum StatusConciliacaoJsonEnum implements Json {
	
	ANDAMENTO ("Em Andamento", "AN"),

	DIVERGENTE("Divergente", "DI"),
	
	OK("OK", "OK"),
	
	ERRO("Erro", "ER"),

	GRAVADA("Gravada", "GR"),
	
	GRAVADA_DIVERGENCIA("Gravada com Divergências", "GD"),
	
	NAO_EXECUTADO("Não Executado", "NE");

	private final String nome;
	
	private final String codigo;

	private StatusConciliacaoJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusConciliacaoJsonEnum fromCodigo(final String codigo) {
		for (StatusConciliacaoJsonEnum tipo : StatusConciliacaoJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	} 
	
}
