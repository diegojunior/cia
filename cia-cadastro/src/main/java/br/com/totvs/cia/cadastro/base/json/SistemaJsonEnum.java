package br.com.totvs.cia.cadastro.base.json;

import lombok.Getter;
import br.com.totvs.cia.infra.json.Json;

@Getter
public enum SistemaJsonEnum implements Json {

	AMPLIS("AMPLIS", "AMP"), JCOT("JCOT", "JCT");

	private final String nome;
	private final String codigo;

	private SistemaJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;

	}

	public static SistemaJsonEnum fromCodigo(final String codigo) {
		for (SistemaJsonEnum tipo : SistemaJsonEnum.values()) {
			if (tipo.getCodigo().equalsIgnoreCase(codigo))
				return tipo;
		}
		return null;
	}
}
