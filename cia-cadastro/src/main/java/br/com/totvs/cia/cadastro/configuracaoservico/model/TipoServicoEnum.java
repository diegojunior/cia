package br.com.totvs.cia.cadastro.configuracaoservico.model;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum TipoServicoEnum implements PersistentEnum {

	POSICAO("Posição", "POS"), 
	MOVIMENTO("Movimento", "MOV");
	
	private final String nome;
	
	private final String codigo;

	private TipoServicoEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static TipoServicoEnum fromCodigo(final String codigo) {
		for (TipoServicoEnum tipo : TipoServicoEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}
	
	public Boolean isPosicao(){
		return this == POSICAO;
	}
	
	public Boolean isMovimento(){
		return this == MOVIMENTO;
	}
}