package br.com.totvs.cia.gateway.core.processamento.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import br.com.totvs.cia.infra.json.Json;

@Data
@AllArgsConstructor
public class CampoProcessamentoJson implements Json {
	
	private static final long serialVersionUID = 1L;

	private String campo;
	
	private String valor;
}
