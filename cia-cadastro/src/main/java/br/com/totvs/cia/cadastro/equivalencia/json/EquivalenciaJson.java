package br.com.totvs.cia.cadastro.equivalencia.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import br.com.totvs.cia.cadastro.equivalencia.model.Equivalencia;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonRootName(value = "equivalencia")
public class EquivalenciaJson implements Json {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String id;
	
	@JsonProperty("idLegado")
	private String idLegado;
	
	@JsonProperty("codigoInterno")
	private String codigoInterno;
	
	@JsonProperty("codigoExterno")
	private String codigoExterno;
	
	public EquivalenciaJson(final Equivalencia model) {
		this.id = model.getId();
		this.idLegado = model.getId();
		this.codigoInterno = model.getCodigoInterno();
		this.codigoExterno = model.getCodigoExterno();
	}
	
}
