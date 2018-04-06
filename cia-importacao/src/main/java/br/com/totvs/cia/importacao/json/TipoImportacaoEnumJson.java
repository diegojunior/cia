package br.com.totvs.cia.importacao.json;

import lombok.Getter;
import br.com.totvs.cia.infra.json.Json;

@Getter
public enum TipoImportacaoEnumJson implements Json {

	ARQUIVO("Arquivo", "ARQ");
	
	private final String nome;
	private final String codigo;

	private TipoImportacaoEnumJson(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;

	}

	public static TipoImportacaoEnumJson fromCodigo(final String codigo) {
		for (TipoImportacaoEnumJson tipo : TipoImportacaoEnumJson.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}
}
