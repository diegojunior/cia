package br.com.totvs.cia.parametrizacao.transformacao.json;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
import lombok.Setter;

public enum TipoTransformacaoEnumJson implements PersistentEnum {

	FIXO("Fixo", "FIX"), 
	EQUIVALENCIA("Equivalência", "EQU");
	
	@Getter
	@Setter
	private String nome;
	
	@Getter
	@Setter
	private String codigo;
	
	private static final Map<String, TipoTransformacaoEnumJson> lookup = new HashMap<String, TipoTransformacaoEnumJson>();
	
	static {
		for (TipoTransformacaoEnumJson tipo : TipoTransformacaoEnumJson.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}

	private TipoTransformacaoEnumJson(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
		
	}
	
	public static TipoTransformacaoEnumJson fromCodigo(final String codigo) {
		for (TipoTransformacaoEnumJson tipo : TipoTransformacaoEnumJson.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
	
	public static TipoTransformacaoEnumJson get(final String codigo) {
		return lookup.get(codigo);
	}
	
	public boolean isFixo() {
		return this == FIXO;
	}
}
