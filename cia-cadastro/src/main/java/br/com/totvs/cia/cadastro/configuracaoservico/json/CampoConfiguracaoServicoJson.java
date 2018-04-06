package br.com.totvs.cia.cadastro.configuracaoservico.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampoConfiguracaoServicoJson implements Json {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "codigo")
	private String campo;
	
	@JsonProperty(value = "label")
	private String label;
	
	public CampoConfiguracaoServicoJson(final CampoConfiguracaoServico model) {
		this.id = model.getId();
		this.campo = model.getCampo();
		this.label = model.getLabel();
	}

}