package br.com.totvs.cia.parametrizacao.unidade.importacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampoParametrizacaoUnidadeImportacaoJson implements Json {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "sessao")
	private SessaoJson sessao;

	@JsonProperty(value = "campo")
	private CampoJson campo;
	
	public CampoParametrizacaoUnidadeImportacaoJson(final String id, final CampoJson campoJson) {
		this.id = id;
		this.campo = campoJson;
	}
}
