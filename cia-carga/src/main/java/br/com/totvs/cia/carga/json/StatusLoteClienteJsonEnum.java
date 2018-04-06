package br.com.totvs.cia.carga.json;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum StatusLoteClienteJsonEnum implements PersistentEnum {
	
	SUCESSO ("Sucesso", "SU"),

	ERRO("Erro", "ER"),
	
	ATENCAO("Atenção", "AT");

	private final String nome;

	private final String codigo;

	private StatusLoteClienteJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusLoteClienteJsonEnum fromCodigo(final String codigo) {
		for (StatusLoteClienteJsonEnum status : StatusLoteClienteJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(status.getCodigo()))
				return status;
		}
		return null;
	}
}
