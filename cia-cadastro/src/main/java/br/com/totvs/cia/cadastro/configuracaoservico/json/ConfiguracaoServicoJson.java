package br.com.totvs.cia.cadastro.configuracaoservico.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumDeserializer;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumSerializer;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracaoServicoJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "id")
	private String id;
	
	@JsonProperty(value = "codigo")
	private String codigo;
	
	@JsonProperty(value = "descricao")
	private String descricao;
	
	@JsonProperty("sistema")
	@JsonSerialize(using = SistemaJsonEnumSerializer.class)
	@JsonDeserialize(using = SistemaJsonEnumDeserializer.class)
	private SistemaJsonEnum sistema;
	
	@JsonProperty("servico")
	@JsonSerialize(using = ServicoJsonEnumSerializer.class)
	@JsonDeserialize(using = ServicoJsonEnumDesirializer.class)
	private ServicoJsonEnum servico;
	
	@JsonProperty("tipoServico")
	@JsonSerialize(using = TipoServicoJsonEnumSerializer.class)
	@JsonDeserialize(using = TipoServicoJsonEnumDesirializer.class)
	private TipoServicoJsonEnum tipoServico;
	
	@JsonProperty("campos")
	private List<CampoConfiguracaoServicoJson> campos;
	
	public ConfiguracaoServicoJson(final ConfiguracaoServico model) {
		this.id = model.getId();
		this.codigo = model.getCodigo();
		this.descricao = model.getDescricao();
		this.sistema = SistemaJsonEnum.fromCodigo(model.getSistema().getCodigo());
		this.servico = ServicoJsonEnum.fromCodigo(model.getServico().getCodigo());
		this.tipoServico = TipoServicoJsonEnum.fromCodigo(model.getTipoServico().getCodigo());
		this.campos = Lists.newArrayList();
	}
	
	public ConfiguracaoServicoJson(final ConfiguracaoServico model, final List<CampoConfiguracaoServicoJson> camposJson) {
		this.id = model.getId();
		this.codigo = model.getCodigo();
		this.descricao = model.getDescricao();
		this.sistema = SistemaJsonEnum.fromCodigo(model.getSistema().getCodigo());
		this.servico = ServicoJsonEnum.fromCodigo(model.getServico().getCodigo());
		this.tipoServico = TipoServicoJsonEnum.fromCodigo(model.getTipoServico().getCodigo());
		this.campos = camposJson;
	}
}