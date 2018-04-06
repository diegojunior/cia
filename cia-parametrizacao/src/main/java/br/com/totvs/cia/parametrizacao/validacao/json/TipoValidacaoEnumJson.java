package br.com.totvs.cia.parametrizacao.validacao.json;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
import lombok.Setter;

public enum TipoValidacaoEnumJson implements PersistentEnum {

	ARQUIVO("Arquivo", "ARQ");
	
	@Getter
	@Setter
	private String nome;
	
	@Getter
	@Setter
	private String codigo;
	
	private static final Map<String, TipoValidacaoEnumJson> lookup = new HashMap<String, TipoValidacaoEnumJson>();
	
	static {
		for (TipoValidacaoEnumJson tipo : TipoValidacaoEnumJson.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}

	private TipoValidacaoEnumJson(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
		
	}
	
	public static TipoValidacaoEnumJson fromCodigo(final String codigo) {
		for (TipoValidacaoEnumJson tipo : TipoValidacaoEnumJson.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
	
	public static TipoValidacaoEnumJson get(final String codigo) {
		return lookup.get(codigo);
	}
}
