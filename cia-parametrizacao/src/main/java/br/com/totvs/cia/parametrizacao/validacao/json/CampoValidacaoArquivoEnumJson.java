package br.com.totvs.cia.parametrizacao.validacao.json;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
import lombok.Setter;

public enum CampoValidacaoArquivoEnumJson implements PersistentEnum {

	CORRETORA("Corretora", "COR"), 
	DATA("Data", "DAT");
	
	@Getter
	@Setter
	private String nome;
	
	@Getter
	@Setter
	private String codigo;
	
	private static final Map<String, CampoValidacaoArquivoEnumJson> lookup = new HashMap<String, CampoValidacaoArquivoEnumJson>();
	
	static {
		for (CampoValidacaoArquivoEnumJson tipo : CampoValidacaoArquivoEnumJson.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}

	private CampoValidacaoArquivoEnumJson(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
		
	}
	
	public static CampoValidacaoArquivoEnumJson fromCodigo(final String codigo) {
		for (CampoValidacaoArquivoEnumJson tipo : CampoValidacaoArquivoEnumJson.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
	
	public static CampoValidacaoArquivoEnumJson get(final String codigo) {
		return lookup.get(codigo);
	}
	
}
