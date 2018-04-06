package br.com.totvs.cia.parametrizacao.layout.json;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
import lombok.Setter;

public enum StatusLayoutEnumJson implements PersistentEnum {

	ATIVO("Ativo", "ATV"),
	INATIVO("Inativo", "ITV");
	
	@Getter
	@Setter
	private String nome;
	
	@Getter
	@Setter
	private String codigo;
	
	private static final Map<String, StatusLayoutEnumJson> lookup = new HashMap<String, StatusLayoutEnumJson>();
	
	static {
		for (StatusLayoutEnumJson tipo : StatusLayoutEnumJson.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}

	private StatusLayoutEnumJson(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
		
	}
	
	public static StatusLayoutEnumJson fromCodigo(final String codigo) {
		for (StatusLayoutEnumJson tipo : StatusLayoutEnumJson.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
	
	public static StatusLayoutEnumJson get(final String codigo) {
		return lookup.get(codigo);
	}
}
