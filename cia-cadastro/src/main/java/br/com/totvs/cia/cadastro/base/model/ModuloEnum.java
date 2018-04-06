package br.com.totvs.cia.cadastro.base.model;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum ModuloEnum implements PersistentEnum {

	CARGA("Carga", "CA"), 
	
	IMPORTACAO("Importação", "IM");

	private final String nome;
	
	private final String codigo;

	private ModuloEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;

	}

	public static ModuloEnum fromCodigo(final String codigo) {
		for (ModuloEnum tipo : ModuloEnum.values()) {
			if (tipo.getCodigo().equalsIgnoreCase(codigo))
				return tipo;
		}
		return null;
	}
	
	public Boolean isCarga () {
		return this == CARGA;
	}
	
	public boolean isImportacao() {
		return this == IMPORTACAO;
	}
}
