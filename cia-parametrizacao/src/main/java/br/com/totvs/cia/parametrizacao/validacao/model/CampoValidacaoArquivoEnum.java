package br.com.totvs.cia.parametrizacao.validacao.model;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum CampoValidacaoArquivoEnum implements PersistentEnum {
	 
	DATA("Data", "DAT"), 
	CORRETORA("Corretora", "COR");

	private String nome;

	private String codigo;
	
	private static final Map<String, CampoValidacaoArquivoEnum> lookup = new HashMap<String, CampoValidacaoArquivoEnum>();
	
	static {
		for (CampoValidacaoArquivoEnum tipo : CampoValidacaoArquivoEnum.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}
	
	private CampoValidacaoArquivoEnum (final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static CampoValidacaoArquivoEnum fromCodigo(final String codigo) {
		for (CampoValidacaoArquivoEnum local : CampoValidacaoArquivoEnum.values()) {
			if (codigo.equalsIgnoreCase(local.getCodigo())) 
				return local;
		}
		return null;
	}
	
	public static CampoValidacaoArquivoEnum get(final String codigo) {
		return lookup.get(codigo);
	}
	
	public boolean isData() {
		return this == DATA;
	}
	
	public boolean isCorretora() {
		return this == CORRETORA;
	}
}