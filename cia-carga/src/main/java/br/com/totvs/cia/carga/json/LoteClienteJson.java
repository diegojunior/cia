package br.com.totvs.cia.carga.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.cadastro.carteira.json.ClienteJson;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoteClienteJson implements Json, Comparable<LoteClienteJson> { 
	
	private static final long serialVersionUID = 1L;

	private String id;

	@JsonProperty("cliente")
	private ClienteJson cliente;
	
	private String dataProcessamento;
	
	@JsonProperty("status")
	@JsonSerialize(using = StatusLoteClienteJsonEnumSerializer.class)
	@JsonDeserialize(using = StatusLoteClienteJsonEnumDeserializer.class)
	private StatusLoteClienteJsonEnum status;
	
	@Override
	public int compareTo(LoteClienteJson outra) {
		return this.cliente.getCodigo().compareTo(outra.getCliente().getCodigo());
	}
}