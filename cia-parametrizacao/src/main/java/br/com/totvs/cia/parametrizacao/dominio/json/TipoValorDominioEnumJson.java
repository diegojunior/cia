package br.com.totvs.cia.parametrizacao.dominio.json;

import lombok.Getter;
import br.com.totvs.cia.infra.json.Json;

@Getter
public enum TipoValorDominioEnumJson implements Json {
	 
	ALFANUMERICO("Alfanumérico", "ALF", 0, 0, 0), 
	NUMERICO("Numérico", "NUM", 0, 0, 0), 
	DATA("Data", "DAT", 0, 0, 0);
	
	@Getter
	private String nome;
	@Getter
	private String codigo;
	@Getter
	private Integer noScale;
	@Getter
	private Integer scale;
	@Getter
	private Integer precisaoPadrao;

	private TipoValorDominioEnumJson(String nome, String codigo, Integer noScale, Integer scale, Integer precisaoPadrao) {
		this.nome = nome;
		this.codigo = codigo;
		this.noScale = noScale;
		this.scale = scale;
		this.precisaoPadrao = precisaoPadrao;
	}
	
	public static TipoValorDominioEnumJson fromCodigo(final String codigo) {
		for (TipoValorDominioEnumJson tipo : TipoValorDominioEnumJson.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}
}