package br.com.totvs.cia.parametrizacao.transformacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJsonDeserializer;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJsonSerializer;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransformacaoJson implements Json {
	
	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "tipoLayout")
	@JsonSerialize(using = TipoLayoutEnumJsonSerializer.class)
	@JsonDeserialize(using = TipoLayoutEnumJsonDeserializer.class)
	private TipoLayoutEnumJson tipoLayout;
	
	@JsonProperty(value = "layout")
	private LayoutJson layout;
	
	@JsonProperty(value = "sessao")
	private SessaoJson sessao;
	
	@JsonProperty(value = "campo")
	private CampoJson campo;
	
	@JsonProperty(value = "tipoTransformacao")
	@JsonSerialize(using = TipoTransformacaoEnumJsonSerializer.class)
	@JsonDeserialize(using = TipoTransformacaoEnumJsonDesirializer.class)
	private TipoTransformacaoEnumJson tipoTransformacao;
	
	@JsonProperty(value = "item")
	private ItemTransformacaoJson item;

	public TransformacaoJson(final Transformacao model, final LayoutJson layout, 
			final SessaoJson sessao, final CampoJson campo, final ItemTransformacaoJson item) {
		this.id = model.getId();
		this.tipoLayout = TipoLayoutEnumJson.fromCodigo(model.getTipoLayout().getCodigo());
		this.layout = layout;
		this.sessao = sessao;
		this.campo = campo;
		this.tipoTransformacao = TipoTransformacaoEnumJson.fromCodigo(model.getTipoTransformacao().getCodigo());
		this.item = item;
	}
}