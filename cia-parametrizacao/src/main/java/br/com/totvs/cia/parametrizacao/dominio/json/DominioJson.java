package br.com.totvs.cia.parametrizacao.dominio.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DominioJson implements Json {
	
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String codigo;

	@JsonProperty("tipo")
	@JsonSerialize(using = TipoValorDominioEnumJsonSerializer.class)
	@JsonDeserialize(using = TipoValorDominioEnumJsonDesirializer.class)
	private TipoValorDominioEnumJson tipo;
	
	public DominioJson(final Dominio model) {
		this.id = model.getId();
		this.codigo = model.getCodigo();
		this.tipo = TipoValorDominioEnumJson.fromCodigo(model.getTipo().getCodigo());
	}
}