package br.com.totvs.cia.parametrizacao.unidade.importacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChaveParametrizacaoUnidadeImportacaoJson implements Json {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "campo")
	private CampoJson campo;

	@JsonProperty(value = "sessoes")
	private List<SessaoJson> sessoes;
	
}
