package br.com.totvs.cia.cadastro.carteira.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClienteJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "idLegado")
	private String idLegado;
	
	@JsonProperty(value = "codigo")
	private String codigo;
	
	@JsonProperty(value = "nome")
	private String nome;

	public ClienteJson(final String cliente) {
		this.codigo = cliente;
	}

	public ClienteJson(final String idLegado, final String codigo, final String nome) {
		this.idLegado = idLegado;
		this.codigo = codigo;
		this.nome = nome;
	}
}