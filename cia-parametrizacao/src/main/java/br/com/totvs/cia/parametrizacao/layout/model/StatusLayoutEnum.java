package br.com.totvs.cia.parametrizacao.layout.model;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
import lombok.Setter;

public enum StatusLayoutEnum implements PersistentEnum {

	ATIVO("Ativo", "ATV"),
	INATIVO("Inativo", "ITV");
	
	@Getter
	@Setter
	private String nome;
	
	@Getter
	@Setter
	private String codigo;
	
	private static final Map<String, StatusLayoutEnum> lookup = new HashMap<String, StatusLayoutEnum>();
	
	static {
		for (StatusLayoutEnum tipo : StatusLayoutEnum.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}

	private StatusLayoutEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
		
	}
	
	public static StatusLayoutEnum fromCodigo(final String codigo) {
		for (StatusLayoutEnum tipo : StatusLayoutEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
	
	public static StatusLayoutEnum get(final String codigo) {
		return lookup.get(codigo);
	}
}
