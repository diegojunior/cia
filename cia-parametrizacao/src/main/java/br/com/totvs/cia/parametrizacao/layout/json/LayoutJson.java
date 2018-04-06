package br.com.totvs.cia.parametrizacao.layout.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.dominio.json.DominioJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LayoutJson implements Json {

	private static final long serialVersionUID = 1L;

	@JsonProperty("id")
	private String id;

	@JsonProperty("codigo")
	private String codigo;

	@JsonProperty("descricao")
	private String descricao;

	@JsonProperty("tipoLayout")
	@JsonDeserialize(using = TipoLayoutEnumJsonDeserializer.class)
	@JsonSerialize(using = TipoLayoutEnumJsonSerializer.class)
	private TipoLayoutEnumJson tipoLayout;

	@JsonProperty("status")
	@JsonDeserialize(using = StatusLayoutEnumJsonDeserializer.class)
	@JsonSerialize(using = StatusLayoutEnumJsonSerializer.class)
	private StatusLayoutEnumJson status;

	@JsonProperty("sessoes")
	private List<SessaoJson> sessoes;
	
	public void addSessao(final SessaoJson sessao) {
		if (this.sessoes == null) {
			this.sessoes = new ArrayList<SessaoJson>();
		}
		this.sessoes.add(sessao);
	}

	public List<SessaoJson> getSessoes() {
		if (this.sessoes == null) {
			this.sessoes = Lists.newArrayList();
		}
		return this.sessoes;
	}

	public List<DominioJson> getDominios() {
		List<DominioJson> dominios = Lists.newArrayList();
		for (SessaoJson sessao : this.getSessoes()) {
			dominios.addAll(sessao.getDominios());
		}
		return dominios;
	}

}
