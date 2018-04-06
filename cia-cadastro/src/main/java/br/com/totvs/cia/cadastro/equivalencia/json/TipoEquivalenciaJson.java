package br.com.totvs.cia.cadastro.equivalencia.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.cadastro.equivalencia.model.TipoEquivalencia;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoEquivalenciaJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "idLegado")
	private String idLegado;
	
	@JsonProperty(value = "codigo")
	private String codigo;
	
	public TipoEquivalenciaJson(final String idLegado, final String codigo) {
		this.idLegado = idLegado;
		this.codigo = codigo;
	}

	public TipoEquivalenciaJson(final TipoEquivalencia model) {
		this.id = model.getId();
		this.idLegado = model.getIdLegado();
		this.codigo = model.getCodigo();
	}
}