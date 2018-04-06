package br.com.totvs.cia.parametrizacao.transformacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemTransformacaoJson implements Json {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "itemFixo")
	private ItemTransformacaoFixoJson itemFixo;
	
	@JsonProperty(value = "itemEquivalencia")
	private ItemTransformacaoEquivalenciaJson itemEquivalencia;
		
	public ItemTransformacaoJson(final ItemTransformacaoFixoJson json) {
		this.itemFixo = json;
		this.itemEquivalencia = new ItemTransformacaoEquivalenciaJson();
	}
	
	public ItemTransformacaoJson(ItemTransformacaoEquivalenciaJson json) {
		this.itemEquivalencia = json;
		this.itemFixo = new ItemTransformacaoFixoJson();
	}

	public Boolean isTipoTransformacaoFixo () {
		return this.itemFixo != null && 
				this.itemFixo.getItens() != null && 
				!this.itemFixo.getItens().isEmpty();
	}
	
	public Boolean isTipoTransformacaoEquivalencia () {
		return this.itemEquivalencia != null && 
				this.itemEquivalencia.getSistema() != null;
	}
}