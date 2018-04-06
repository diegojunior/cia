package br.com.totvs.cia.parametrizacao.layout.json;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
import lombok.Setter;

public enum TipoLayoutEnumJson implements PersistentEnum {

	POSICIONAL("TXT Posicional", "POS"), DELIMITADOR("Delimitador", "DEL"), XML("XML", "XML");

	@Getter
	@Setter
	private String nome;

	@Getter
	@Setter
	private String codigo;

	private static final Map<String, TipoLayoutEnumJson> lookup = new HashMap<String, TipoLayoutEnumJson>();

	static {
		for (final TipoLayoutEnumJson tipo : TipoLayoutEnumJson.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}

	private TipoLayoutEnumJson(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;

	}

	public static TipoLayoutEnumJson fromCodigo(final String codigo) {
		for (final TipoLayoutEnumJson tipo : TipoLayoutEnumJson.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}

	public static TipoLayoutEnumJson get(final String codigo) {
		return lookup.get(codigo);
	}
}
