package br.com.totvs.cia.parametrizacao.validacao.json;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
import lombok.Setter;

public enum LocalValidacaoArquivoEnumJson implements PersistentEnum {

	EXTERNO("Externo", "EXT"), 
	INTERNO("Interno", "INT");
	
	@Getter
	@Setter
	private String nome;
	
	@Getter
	@Setter
	private String codigo;
	
	private static final Map<String, LocalValidacaoArquivoEnumJson> lookup = new HashMap<String, LocalValidacaoArquivoEnumJson>();
	
	static {
		for (LocalValidacaoArquivoEnumJson tipo : LocalValidacaoArquivoEnumJson.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}

	private LocalValidacaoArquivoEnumJson(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
		
	}
	
	public static LocalValidacaoArquivoEnumJson fromCodigo(final String codigo) {
		for (LocalValidacaoArquivoEnumJson tipo : LocalValidacaoArquivoEnumJson.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
	
	public static LocalValidacaoArquivoEnumJson get(final String codigo) {
		return lookup.get(codigo);
	}
	
	public Boolean isExterno () {
		return this == EXTERNO;
	}
	
	public Boolean isInterno () {
		return this == INTERNO;
	}
}
