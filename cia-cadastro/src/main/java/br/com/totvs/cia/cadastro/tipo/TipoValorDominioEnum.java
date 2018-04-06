package br.com.totvs.cia.cadastro.tipo;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum TipoValorDominioEnum implements PersistentEnum {
	 
	ALFANUMERICO("Alfanumérico", "ALF", 0, 0, 0), 
	NUMERICO("Numérico", "NUM", 15, 0, 15), 
	DECIMAL("Decimal", "DEC", 15, 12, 27), 
	DATA("DATA", "DAT", 0, 0, 0);
	
	private String nome;

	private String codigo;
	
	private Integer noScale;
	
	private Integer scale;
	
	private Integer precisaoPadrao;

	private TipoValorDominioEnum(final String nome, final String codigo, final Integer no_scale, final Integer scale, final Integer precisaoPadrao) {
		this.nome = nome;
		this.codigo = codigo;
		
		this.noScale = no_scale;
		this.scale = scale;
		this.precisaoPadrao = precisaoPadrao;
	}
	
	public static TipoValorDominioEnum fromCodigo(final String codigo) {
		for (final TipoValorDominioEnum tipo : TipoValorDominioEnum.values()) {
			if (tipo.getCodigo().equalsIgnoreCase(codigo))
				return tipo;
		}
		return null;
	}
	
	public Boolean isEfetuaCalculo() {
		return this.equals(TipoValorDominioEnum.NUMERICO) 
				|| this.equals(TipoValorDominioEnum.DECIMAL);
	}
	
	public Boolean isDecimal() {
		return this.equals(TipoValorDominioEnum.DECIMAL);
	}
	
	public Boolean isDate() {
		return this.equals(TipoValorDominioEnum.DATA);
	}
	
}