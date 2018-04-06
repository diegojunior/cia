package br.com.totvs.cia.importacao.model;

import lombok.Getter;
import br.com.totvs.cia.infra.enums.PersistentEnum;
@Getter
public enum TipoImportacaoEnum implements PersistentEnum {

	ARQUIVO("Arquivo", "ARQ");
	
	private final String nome;
	private final String codigo;

	private TipoImportacaoEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static TipoImportacaoEnum fromCodigo(final String codigo) {
		for (TipoImportacaoEnum tipo : TipoImportacaoEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}
}
