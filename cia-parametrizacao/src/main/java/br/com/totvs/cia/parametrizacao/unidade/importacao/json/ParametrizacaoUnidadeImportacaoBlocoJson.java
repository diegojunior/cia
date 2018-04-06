package br.com.totvs.cia.parametrizacao.unidade.importacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametrizacaoUnidadeImportacaoBlocoJson implements Json {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "codigo")
	private String codigo;
	
	@JsonProperty(value = "descricao")
	private String descricao;

	@JsonProperty(value = "layout")
	private LayoutJson layout;
	
	@JsonProperty(value = "sessaoAbertura")
	private SessaoJson sessaoAbertura;
	
	@JsonProperty(value = "sessaoFechamento")
	private SessaoJson sessaoFechamento;

	@JsonProperty("linhasBloco")
	private List<LinhaBlocoParametrizacaoUnidadeImportacaoJson> linhas;
}