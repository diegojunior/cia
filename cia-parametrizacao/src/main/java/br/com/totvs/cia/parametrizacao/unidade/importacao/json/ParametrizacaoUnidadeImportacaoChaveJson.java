package br.com.totvs.cia.parametrizacao.unidade.importacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJsonDeserializer;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJsonSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametrizacaoUnidadeImportacaoChaveJson implements Json {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "codigo")
	private String codigo;
	
	@JsonProperty(value = "descricao")
	private String descricao;

	@JsonProperty(value = "tipoLayout")
	@JsonSerialize(using = TipoLayoutEnumJsonSerializer.class)
	@JsonDeserialize(using = TipoLayoutEnumJsonDeserializer.class)
	private TipoLayoutEnumJson tipoLayout;

	@JsonProperty(value = "layout")
	private LayoutJson layout;

	@JsonProperty("camposUnidadeImportacao")
	private List<CampoParametrizacaoUnidadeImportacaoJson> camposUnidadeImportacao;
	
	@JsonProperty("chavesUnidadeImportacao")
	private List<ChaveParametrizacaoUnidadeImportacaoJson> chavesUnidadeImportacao;

	@JsonProperty("sessoes")
	private List<SessaoJson> sessoes;
	
	public List<SessaoJson> getSessoes() {
		if (this.sessoes == null) {
			return this.sessoes = Lists.newArrayList();
		}
		return this.sessoes;
	}

	public List<CampoParametrizacaoUnidadeImportacaoJson> getCamposUnidadeImportacao() {
		if (this.camposUnidadeImportacao == null) {
			return this.camposUnidadeImportacao = Lists.newArrayList();
		}
		return this.camposUnidadeImportacao;
	}
	
	public List<ChaveParametrizacaoUnidadeImportacaoJson> getChavesUnidadeImportacao() {
		if (this.chavesUnidadeImportacao == null) {
			return this.chavesUnidadeImportacao = Lists.newArrayList();
		}
		return this.chavesUnidadeImportacao;
	}

}
