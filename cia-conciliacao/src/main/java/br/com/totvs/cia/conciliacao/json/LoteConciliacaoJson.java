package br.com.totvs.cia.conciliacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import br.com.totvs.cia.conciliacao.model.LoteConciliacao;
import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.ConfiguracaoPerfilJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoteConciliacaoJson implements Json {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "configuracaoPerfil")
	private ConfiguracaoPerfilJson configuracaoPerfil;

	@JsonProperty(value = "unidades")
	private List<UnidadeConciliacaoJson> unidades;
	
	public LoteConciliacaoJson(final LoteConciliacao model, final ConfiguracaoPerfilJson configuracaoPerfil, 
			final List<UnidadeConciliacaoJson> unidades) {
		this.id = model.getId();
		this.configuracaoPerfil = configuracaoPerfil;
		this.unidades = unidades;
	}

	public LoteConciliacaoJson(final LoteConciliacao model, final ConfiguracaoPerfilJson configuracaoPerfil) {
		this.id = model.getId();
		this.configuracaoPerfil = configuracaoPerfil;
		this.unidades = Lists.newArrayList();
	}
}