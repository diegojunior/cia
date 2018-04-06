package br.com.totvs.cia.parametrizacao.perfilconciliacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilConciliacaoJson implements Json {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	@JsonProperty(value = "identificacao")
	private IdentificacaoJson identificacao;
	
	@JsonProperty(value = "configuracao")
	private ConfiguracaoPerfilJson configuracao;
	
	@JsonProperty(value = "regras")
	private List<RegraPerfilJson> regras;
	
	public PerfilConciliacaoJson(final PerfilConciliacao model, final LayoutJson layout, final ConfiguracaoPerfilJson configuracao) {
		this.id = model.getId();
		this.identificacao = new IdentificacaoJson(model, layout);
		this.configuracao = configuracao;
	}
	
	public PerfilConciliacaoJson(final PerfilConciliacao model, final LayoutJson layout, final ConfiguracaoPerfilJson configuracao,
			final List<RegraPerfilJson> regras) {
		this.id = model.getId();
		this.identificacao = new IdentificacaoJson(model, layout);
		this.configuracao = configuracao;
		this.regras = regras;
	}
}