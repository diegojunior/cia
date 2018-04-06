package br.com.totvs.cia.parametrizacao.transformacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumDeserializer;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumSerializer;
import br.com.totvs.cia.cadastro.equivalencia.json.RemetenteJson;
import br.com.totvs.cia.cadastro.equivalencia.json.TipoEquivalenciaJson;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemTransformacaoEquivalenciaJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "sistema")
	@JsonSerialize(using = SistemaJsonEnumSerializer.class)
	@JsonDeserialize(using = SistemaJsonEnumDeserializer.class)
	private SistemaJsonEnum sistema;
	
	@JsonProperty(value = "remetente")
	private RemetenteJson remetente;
	
	@JsonProperty(value = "tipoEquivalencia")
	private TipoEquivalenciaJson tipoEquivalencia;
}