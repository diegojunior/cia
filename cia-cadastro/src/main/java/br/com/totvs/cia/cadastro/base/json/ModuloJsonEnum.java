package br.com.totvs.cia.cadastro.base.json;

import br.com.totvs.cia.infra.json.Json;
import lombok.Getter;

@Getter
public enum ModuloJsonEnum implements Json {

	CARGA("Carga", "CA"), 
	
	IMPORTACAO("Importação", "IM");

	private final String nome;
	
	private final String codigo;

	private ModuloJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;

	}

	public static ModuloJsonEnum fromCodigo(final String codigo) {
		for (ModuloJsonEnum tipo : ModuloJsonEnum.values()) {
			if (tipo.getCodigo().equalsIgnoreCase(codigo))
				return tipo;
		}
		return null;
	}

	public boolean isCarga() {
		return this == CARGA;
	}
	
	public boolean isImportacao() {
		return this == IMPORTACAO;
	}
}
