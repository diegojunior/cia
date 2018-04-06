package br.com.totvs.cia.importacao.json;

import br.com.totvs.cia.infra.json.Json;
import lombok.Getter;

@Getter
public enum StatusImportacaoJsonEnum implements Json {

	ANDAMENTO("Em Andamento", "AN"),
	CONCLUIDO("Concluído", "CO"), 
	ERRO("Erro", "ER"),
	NAO_EXECUTADO("Não Executado", "NE");

	private final String nome;
	
	private final String codigo;


	private StatusImportacaoJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusImportacaoJsonEnum fromCodigo(final String codigo) {
		for (final StatusImportacaoJsonEnum tipo : StatusImportacaoJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	} 
	
}
