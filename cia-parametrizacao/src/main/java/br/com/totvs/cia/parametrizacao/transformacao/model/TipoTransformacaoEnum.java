package br.com.totvs.cia.parametrizacao.transformacao.model;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum TipoTransformacaoEnum implements PersistentEnum {
	 
	FIXO("Fixo", "FIX"), 
	EQUIVALENCIA("Equivalência", "EQU");

	private String nome;

	private String codigo;
	
	private static final Map<String, TipoTransformacaoEnum> lookup = new HashMap<String, TipoTransformacaoEnum>();
	
	static {
		for (TipoTransformacaoEnum tipo : TipoTransformacaoEnum.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}
	
	private TipoTransformacaoEnum (final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static TipoTransformacaoEnum fromCodigo(final String codigo) {
		for (TipoTransformacaoEnum tipo : TipoTransformacaoEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
	
	public static TipoTransformacaoEnum get(final String codigo) {
		return lookup.get(codigo);
	}
	
	public boolean isFixo() {
		return this == FIXO;
	}
}