package br.com.totvs.cia.parametrizacao.validacao.model;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum LocalValidacaoArquivoEnum implements PersistentEnum {
	 
	EXTERNO("Externo", "EXT"), 
	INTERNO("Interno", "INT");

	private String nome;

	private String codigo;
	
	private static final Map<String, LocalValidacaoArquivoEnum> lookup = new HashMap<String, LocalValidacaoArquivoEnum>();
	
	static {
		for (LocalValidacaoArquivoEnum tipo : LocalValidacaoArquivoEnum.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}
	
	private LocalValidacaoArquivoEnum (final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static LocalValidacaoArquivoEnum fromCodigo(final String codigo) {
		for (LocalValidacaoArquivoEnum local : LocalValidacaoArquivoEnum.values()) {
			if (codigo.equalsIgnoreCase(local.getCodigo())) 
				return local;
		}
		return null;
	}
	
	public static LocalValidacaoArquivoEnum get(final String codigo) {
		return lookup.get(codigo);
	}
	
	public boolean isExterno() {
		return this == EXTERNO;
	}
	
	public boolean isInterno() {
		return this == INTERNO;
	}
}