package br.com.totvs.cia.importacao.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.totvs.cia.infra.json.Json;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoJson implements Json {
	
	private static final long serialVersionUID = 1L;

	private String id;
	
	private String fileName;
	
	private Long tamanho;
	
}
