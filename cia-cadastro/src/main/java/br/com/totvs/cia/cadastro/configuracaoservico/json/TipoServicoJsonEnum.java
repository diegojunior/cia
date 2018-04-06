package br.com.totvs.cia.cadastro.configuracaoservico.json;

import br.com.totvs.cia.infra.json.Json;
import lombok.Getter;

@Getter
public enum TipoServicoJsonEnum  implements Json {

	POSICAO("Posição", "POS"), 
	MOVIMENTO("Movimento", "MOV");

	private final String nome;
	private final String codigo;

	private TipoServicoJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;

	}

	public static TipoServicoJsonEnum fromCodigo(final String codigo) {
		for (TipoServicoJsonEnum tipo : TipoServicoJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}
	
}
