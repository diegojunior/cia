package br.com.totvs.cia.parametrizacao.layout.model;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
import lombok.Setter;

public enum TipoDelimitadorEnum implements PersistentEnum {

	TABULAR("Tabular", "TAB"),
	PONTO_VIRGULA("Ponto-Virgula", "PVA"),
	VIRGULA("Virgula", "VIR");
	
	@Getter
	@Setter
	private String nome;
	
	@Getter
	@Setter
	private String codigo;
	
	private static final Map<String, TipoDelimitadorEnum> lookup = new HashMap<String, TipoDelimitadorEnum>();
	
	static {
		for (TipoDelimitadorEnum tipo : TipoDelimitadorEnum.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}

	private TipoDelimitadorEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
		
	}
	
	public static TipoDelimitadorEnum fromCodigo(final String codigo) {
		for (TipoDelimitadorEnum tipo : TipoDelimitadorEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
	
	public static TipoDelimitadorEnum get(final String codigo) {
		return lookup.get(codigo);
	}
	
	public String getValorTipoDelimitador() {
		switch (this) {
			case TABULAR:
				return "\t";
			case PONTO_VIRGULA:
				return ";";
			case VIRGULA:
				return ",";
			default:
				throw new IllegalArgumentException("Tipo de delimitador não identificado.");
		}
	}
	
}
