package br.com.totvs.cia.cadastro.base.json;

import br.com.totvs.cia.infra.json.Json;
import lombok.Getter;

@Getter
public enum CondicaoJsonEnum implements Json {

	COMECA_COM("Começa Com", "CC"), 
	
	CONTEM("Contém", "CO"),
	
	DIFERENTE("Diferente", "DI"), 
	
	IGUAL("Igual", "IG");

	private final String nome;
	
	private final String codigo;

	private CondicaoJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;

	}

	public static CondicaoJsonEnum fromCodigo(final String codigo) {
		for (CondicaoJsonEnum tipo : CondicaoJsonEnum.values()) {
			if (tipo.getCodigo().equalsIgnoreCase(codigo))
				return tipo;
		}
		return null;
	}
}
