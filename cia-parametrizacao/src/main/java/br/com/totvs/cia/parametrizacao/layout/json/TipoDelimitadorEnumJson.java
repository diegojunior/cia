package br.com.totvs.cia.parametrizacao.layout.json;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
import lombok.Setter;

public enum TipoDelimitadorEnumJson implements PersistentEnum {

	TABULAR("Tabular", "TAB"),
	PONTO_VIRGULA("Ponto-Virgula", "PVA"),
	VIRGULA("Virgula", "VIR");

	@Getter
	@Setter
	private String nome;

	@Getter
	@Setter
	private String codigo;

	private static final Map<String, TipoDelimitadorEnumJson> lookup = new HashMap<String, TipoDelimitadorEnumJson>();

	static {
		for (final TipoDelimitadorEnumJson tipo : TipoDelimitadorEnumJson.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}

	private TipoDelimitadorEnumJson(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;

	}

	public static TipoDelimitadorEnumJson fromCodigo(final String codigo) {
		for (final TipoDelimitadorEnumJson tipo : TipoDelimitadorEnumJson.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}

	public static TipoDelimitadorEnumJson get(final String codigo) {
		return lookup.get(codigo);
	}
}
