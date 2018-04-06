package br.com.totvs.cia.importacao.json;

import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampoImportacaoProcessamentoJson implements Json {
	
	private static final long serialVersionUID = 1L;

	private String campo;
	
	private String valor;
	
	private String pattern;
}
