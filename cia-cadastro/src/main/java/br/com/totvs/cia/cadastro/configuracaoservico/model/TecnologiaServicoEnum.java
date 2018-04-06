package br.com.totvs.cia.cadastro.configuracaoservico.model;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
@Getter
public enum TecnologiaServicoEnum implements PersistentEnum {

	REST("REST", "RST"), SOAP("SOAP", "SOP");
	
	private final String nome;
	
	private final String codigo;

	private TecnologiaServicoEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static TecnologiaServicoEnum fromCodigo(final String codigo) {
		for (TecnologiaServicoEnum tipo : TecnologiaServicoEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}
}
