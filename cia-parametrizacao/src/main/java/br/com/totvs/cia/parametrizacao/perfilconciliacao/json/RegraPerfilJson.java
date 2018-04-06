package br.com.totvs.cia.parametrizacao.perfilconciliacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.cadastro.base.json.CondicaoJsonEnum;
import br.com.totvs.cia.cadastro.base.json.CondicaoJsonEnumDeserializer;
import br.com.totvs.cia.cadastro.base.json.CondicaoJsonEnumSerializer;
import br.com.totvs.cia.cadastro.base.json.ModuloJsonEnum;
import br.com.totvs.cia.cadastro.base.json.ModuloJsonEnumDeserializer;
import br.com.totvs.cia.cadastro.base.json.ModuloJsonEnumSerializer;
import br.com.totvs.cia.cadastro.configuracaoservico.json.CampoConfiguracaoServicoJson;
import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.Regra;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegraPerfilJson implements Json {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("modulo")
	@JsonDeserialize(using = ModuloJsonEnumDeserializer.class)
	@JsonSerialize(using = ModuloJsonEnumSerializer.class)
	private ModuloJsonEnum modulo;
	
	@JsonProperty("campoCarga")
	private CampoConfiguracaoServicoJson campoCarga;
	
	@JsonProperty("campoImportacao")
	private String campoImportacao;
	
	@JsonProperty("condicao")
	@JsonDeserialize(using = CondicaoJsonEnumDeserializer.class)
	@JsonSerialize(using = CondicaoJsonEnumSerializer.class)
	private CondicaoJsonEnum condicao;
	
	@JsonProperty("filtro")
	private String filtro;
	
	public RegraPerfilJson(final Regra model, final CampoConfiguracaoServicoJson campoCarga, final String campoImportacao) {
		this.modulo = ModuloJsonEnum.fromCodigo(model.getModulo().getCodigo());
		this.condicao = CondicaoJsonEnum.fromCodigo(model.getCondicao().getCodigo());
		this.filtro = model.getFiltro();
		this.campoCarga = campoCarga;
		this.campoImportacao = campoImportacao;
	}
}