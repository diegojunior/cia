package br.com.totvs.cia.parametrizacao.unidade.importacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampoUnidadeImportacaoResumidoJson implements Json {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "codigo")
	private String codigo;
	
}
