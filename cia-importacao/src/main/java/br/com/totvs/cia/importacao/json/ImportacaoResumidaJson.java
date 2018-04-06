package br.com.totvs.cia.importacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumDeserializer;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumSerializer;
import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportacaoResumidaJson implements Json {
	
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String data;
	
	@JsonProperty("sistema")
	@JsonSerialize(using = SistemaJsonEnumSerializer.class)
	@JsonDeserialize(using = SistemaJsonEnumDeserializer.class)
	private SistemaJsonEnum sistema;
	
	@JsonProperty(value = "layout")
	private LayoutJson layout;
}