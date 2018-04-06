package br.com.totvs.cia.parametrizacao.transformacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixoDePara;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemTransformacaoFixoDeParaJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("de")
	private String de;
	
	@JsonProperty("para")
	private String para;
	
	public ItemTransformacaoFixoDeParaJson(final ItemTransformacaoFixoDePara model) {
		this.id = model.getId();
		this.de = model.getDe();
		this.para = model.getPara();
	}

	public ItemTransformacaoFixoDeParaJson(final ItemTransformacaoFixoDeParaJson itemDePara) {
		this.id = itemDePara.getId();
		this.de = itemDePara.getDe();
		this.para = itemDePara.getPara();
	}

}