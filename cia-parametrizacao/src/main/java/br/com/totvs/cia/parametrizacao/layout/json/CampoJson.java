package br.com.totvs.cia.parametrizacao.layout.json;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.dominio.json.DominioJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "campo")
public class CampoJson implements Json {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "ordem")
	private Integer ordem;

	@JsonProperty(value = "dominio")
	private DominioJson dominio;

	@JsonProperty(value = "descricao")
	private String descricao;

	@JsonProperty(value = "tag")
	private String tag;

	@JsonProperty(value = "tamanho")
	private Integer tamanho;

	@JsonProperty(value = "posicaoInicial")
	private Integer posicaoInicial;

	@JsonProperty(value = "posicaoFinal")
	private Integer posicaoFinal;

	@JsonProperty("pattern")
	private String pattern;

	@JsonGetter("codigo")
	public String getCodigo() {
		return this.getDominio().getCodigo();
	}

}
