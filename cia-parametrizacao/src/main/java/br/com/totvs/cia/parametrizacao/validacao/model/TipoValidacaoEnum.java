package br.com.totvs.cia.parametrizacao.validacao.model;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum TipoValidacaoEnum implements PersistentEnum {
	 
	ARQUIVO("Arquivo", "ARQ");

	private String nome;

	private String codigo;
	
	private static final Map<String, TipoValidacaoEnum> lookup = new HashMap<String, TipoValidacaoEnum>();
	
	static {
		for (TipoValidacaoEnum tipo : TipoValidacaoEnum.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}
	
	private TipoValidacaoEnum (final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static TipoValidacaoEnum fromCodigo(final String codigo) {
		for (TipoValidacaoEnum tipo : TipoValidacaoEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
	
	public static TipoValidacaoEnum get(final String codigo) {
		return lookup.get(codigo);
	}
}