package br.com.totvs.cia.parametrizacao.layout.model;

import lombok.Getter;
import br.com.totvs.cia.infra.enums.PersistentEnum;

public enum DelimitadorEnum implements PersistentEnum {

	PIPE("Pipe Caracter", "|"), ESPACO("Espaço Branco", "\\s");
	
	@Getter
	private String nome;
	
	@Getter
	private String codigo;

	private DelimitadorEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static DelimitadorEnum fromCodigo(final String codigo) {
		for (DelimitadorEnum tipo : DelimitadorEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
}
