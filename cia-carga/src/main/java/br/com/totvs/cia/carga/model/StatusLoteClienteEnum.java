package br.com.totvs.cia.carga.model;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum StatusLoteClienteEnum implements PersistentEnum{

	SUCESSO("Sucesso", "SU"),
	
	ERRO("Erro", "ER"),
	
	ATENCAO("Atenção", "AT");

	private final String nome;

	private final String codigo;

	private StatusLoteClienteEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static StatusLoteClienteEnum fromCodigo(final String codigo) {
		for (StatusLoteClienteEnum status : StatusLoteClienteEnum.values()) {
			if (codigo.equalsIgnoreCase(status.getCodigo()))
				return status;
		}
		return null;
	}	
}