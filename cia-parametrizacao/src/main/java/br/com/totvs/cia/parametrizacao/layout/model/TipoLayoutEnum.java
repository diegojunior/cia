package br.com.totvs.cia.parametrizacao.layout.model;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
import lombok.Setter;

public enum TipoLayoutEnum implements PersistentEnum {

	POSICIONAL("TXT Posicional", "POS"), 
	XML("XML", "XML"), 
	DELIMITADOR("Delimitador", "DEL");

	@Getter
	@Setter
	private String nome;

	@Getter
	@Setter
	private String codigo;

	private static final Map<String, TipoLayoutEnum> lookup = new HashMap<String, TipoLayoutEnum>();

	static {
		for (final TipoLayoutEnum tipo : TipoLayoutEnum.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}

	private TipoLayoutEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;

	}

	public static TipoLayoutEnum fromCodigo(final String codigo) {
		for (final TipoLayoutEnum tipo : TipoLayoutEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}

	public static TipoLayoutEnum get(final String codigo) {
		return lookup.get(codigo);
	}
	
	public boolean isDelimitador() {
		return this == DELIMITADOR;
	}
	
	public boolean isPosicional() {
		return this == POSICIONAL;
	}
	
	public boolean isXml() {
		return this == XML;
	}
}