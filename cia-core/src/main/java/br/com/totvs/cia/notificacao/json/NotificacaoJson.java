package br.com.totvs.cia.notificacao.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacaoJson implements Json {

	private static final long serialVersionUID = 1L;
	
    private String id;
	
	@JsonProperty(value = "data")
	private Date data;
	
	@JsonProperty("status")
	@JsonSerialize(using = StatusEnumJsonSerializer.class)
	@JsonDeserialize(using = StatusEnumJsonDesirializer.class)
	private StatusEnumJson status;
	
	@JsonProperty(value = "mensagem")
	private String mensagem;

}
