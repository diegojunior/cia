package br.com.totvs.cia.parametrizacao.transformacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.infra.json.Json;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemTransformacaoFixoJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "itensDePara")
	private List<ItemTransformacaoFixoDeParaJson> itens;
	
	public ItemTransformacaoFixoJson(final String id, final List<ItemTransformacaoFixoDeParaJson> itensDePara) {
		this.id = id;
		this.itens = itensDePara;
	}
	
}