package br.com.totvs.cia.cadastro.grupo.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.cadastro.grupo.model.Grupo;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrupoJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "idLegado")
	private String idLegado;
	
	@JsonProperty(value = "codigo")
	private String codigo;

	public GrupoJson(final Grupo model) {
		this.id = model.getId();
		this.idLegado = model.getIdLegado();
		this.codigo = model.getCodigo();
	}

	public GrupoJson(final String idLegado, final String codigo) {
		this.idLegado = idLegado;
		this.codigo = codigo;
	}
}