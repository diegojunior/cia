package br.com.totvs.cia.importacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.agente.json.AgenteJson;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumDeserializer;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumSerializer;
import br.com.totvs.cia.cadastro.equivalencia.json.EquivalenciaJson;
import br.com.totvs.cia.cadastro.equivalencia.json.RemetenteJson;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportacaoJson implements Json {
	
	private static final long serialVersionUID = 1L;

	private String id;
	
	@JsonProperty(value = "dataImportacao")
	private String dataImportacao;
	
	@JsonSerialize(using = StatusImportacaoEnumJsonSerializer.class)
	@JsonDeserialize(using = StatusImportacaoEnumJsonDeserializer.class)
	@JsonProperty(value = "status")
	private StatusImportacaoJsonEnum status;
	
	@JsonSerialize(using = SistemaJsonEnumSerializer.class)
	@JsonDeserialize(using = SistemaJsonEnumDeserializer.class)
	@JsonProperty("sistema")
	private SistemaJsonEnum sistema;
	
	@JsonProperty(value = "layout")
	private LayoutJson layout;
	
	@JsonSerialize(using = TipoImportacaoEnumJsonSerializer.class)
	@JsonDeserialize(using = TipoImportacaoEnumJsonDesirializer.class)
	@JsonProperty("tipoImportacao")
	private TipoImportacaoEnumJson tipoImportacao;
	
	@JsonProperty("arquivo")
	private ArquivoJson arquivo;
	
	@JsonProperty("agente")
	private AgenteJson agente;
	
	@JsonProperty("remetente")
	private RemetenteJson remetente;
	
	@JsonProperty("equivalencias")
	private List<EquivalenciaJson> equivalencias;
	
	
	public ImportacaoJson(final Importacao model) {
		this.id = model.getId();
		this.dataImportacao = DateUtil.format(model.getDataImportacao(), DateUtil.yyyyMMdd);
		this.status = StatusImportacaoJsonEnum.fromCodigo(model.getStatus().getCodigo());
		this.sistema = model.getSistema() != null ? SistemaJsonEnum.fromCodigo(model.getSistema().getCodigo()) : null;
		this.layout = new LayoutJson(model.getLayout().getId(), 
				model.getLayout().getCodigo(), model.getLayout().getDescricao(), 
				TipoLayoutEnumJson.fromCodigo(model.getLayout().getTipoLayout().getCodigo()), 
				null, Lists.newArrayList());
	}


}
