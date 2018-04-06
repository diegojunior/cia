package br.com.totvs.cia.parametrizacao.validacao.json;

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
import br.com.totvs.cia.parametrizacao.validacao.model.ValidacaoArquivoExterno;
import br.com.totvs.cia.parametrizacao.validacao.model.ValidacaoArquivoInterno;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidacaoArquivoJson implements Json {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "campoValidacao")
	@JsonSerialize(using = CampoValidacaoArquivoEnumJsonSerializer.class)
	@JsonDeserialize(using = CampoValidacaoArquivoEnumJsonDeserializer.class)
	private CampoValidacaoArquivoEnumJson campoValidacao;
	
	@JsonProperty(value = "tipoLayout")
	@JsonSerialize(using = TipoLayoutEnumJsonSerializer.class)
	@JsonDeserialize(using = TipoLayoutEnumJsonDeserializer.class)
	private TipoLayoutEnumJson tipoLayout;
	
	@JsonProperty(value = "layout")
	private LayoutJson layout;
	
	@JsonProperty(value = "localValidacao")
	@JsonSerialize(using = LocalValidacaoArquivoEnumJsonSerializer.class)
	@JsonDeserialize(using = LocalValidacaoArquivoEnumJsonDeserializer.class)
	private LocalValidacaoArquivoEnumJson localValidacao;
	
	@JsonProperty(value = "posicaoInicial")
	private Integer posicaoInicial;
	
	@JsonProperty(value = "posicaoFinal")
	private Integer posicaoFinal;
	
	@JsonProperty(value = "formato")
	private String formato;
	
	@JsonProperty(value = "sessaoLayout")
	private SessaoJson sessaoLayout;
	
	@JsonProperty(value = "campoLayout")
	private CampoJson campoLayout;
	
	public ValidacaoArquivoJson(final ValidacaoArquivoExterno externo, final LayoutJson layout) {
		this.id = externo.getId();
		this.campoValidacao = CampoValidacaoArquivoEnumJson.fromCodigo(externo.getCampoValidacao().getCodigo());
		this.tipoLayout = TipoLayoutEnumJson.fromCodigo(externo.getTipoLayout().getCodigo());
		this.layout = layout;
		this.localValidacao = LocalValidacaoArquivoEnumJson.EXTERNO;
		this.posicaoInicial = externo.getPosicaoInicial();
		this.posicaoFinal = externo.getPosicaoFinal();
		this.formato = externo.getPattern();
	}
	
	public ValidacaoArquivoJson(final ValidacaoArquivoInterno interno, final LayoutJson layout, 
			final SessaoJson sessaoLayout, final CampoJson campoLayout) {
		this.id = interno.getId();
		this.campoValidacao = CampoValidacaoArquivoEnumJson.fromCodigo(interno.getCampoValidacao().getCodigo());
		this.tipoLayout = TipoLayoutEnumJson.fromCodigo(interno.getTipoLayout().getCodigo());
		this.layout = layout;
		this.localValidacao = LocalValidacaoArquivoEnumJson.INTERNO;
		this.sessaoLayout = sessaoLayout;
		this.campoLayout = campoLayout;
	}
}