package br.com.totvs.cia.importacao.model;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum StatusImportacaoEnum implements PersistentEnum {
	
	ANDAMENTO("Em Andamento", "AN"),
	CONCLUIDO("Concluído", "CO"), 
	ERRO("Erro", "ER"),
	NAO_EXECUTADO("Não Executado", "NE");

	private final String nome;
	
	private final String codigo;

	private StatusImportacaoEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusImportacaoEnum fromCodigo(final String codigo) {
		for (final StatusImportacaoEnum tipo : StatusImportacaoEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	} 
	
}
