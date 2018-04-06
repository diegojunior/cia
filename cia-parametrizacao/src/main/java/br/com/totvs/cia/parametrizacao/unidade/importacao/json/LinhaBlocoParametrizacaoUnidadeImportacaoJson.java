package br.com.totvs.cia.parametrizacao.unidade.importacao.json;

import java.util.List;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinhaBlocoParametrizacaoUnidadeImportacaoJson implements Json {

	private static final long serialVersionUID = 1L;
	
	private String id;

	private SessaoJson sessao;
	
	private List<CampoJson> campos;
}