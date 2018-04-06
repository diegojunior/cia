package br.com.totvs.cia.conciliacao.json;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.carga.json.StatusCargaJsonEnum;
import br.com.totvs.cia.carga.json.StatusCargaJsonEnumDesirializer;
import br.com.totvs.cia.carga.json.StatusCargaJsonEnumSerializer;
import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.model.ConciliacaoForExecution;
import br.com.totvs.cia.importacao.json.StatusImportacaoEnumJsonDeserializer;
import br.com.totvs.cia.importacao.json.StatusImportacaoEnumJsonSerializer;
import br.com.totvs.cia.importacao.json.StatusImportacaoJsonEnum;
import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.infra.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConciliacaoExecucaoJson implements Json {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("data")
	private String data;
	
	@JsonProperty("perfil")
	private String perfil;
	
	@JsonProperty("layout")
	private String layout;
	
	@JsonProperty("carga")
	private String carga;
	
	@JsonProperty("importacao")
	private String importacao;
	
	@JsonProperty("statusCarga")
	@JsonSerialize(using = StatusCargaJsonEnumSerializer.class)
	@JsonDeserialize(using = StatusCargaJsonEnumDesirializer.class)
	private StatusCargaJsonEnum statusCarga;
	
	@JsonProperty("statusImportacao")
	@JsonSerialize(using = StatusImportacaoEnumJsonSerializer.class)
	@JsonDeserialize(using = StatusImportacaoEnumJsonDeserializer.class)
	private StatusImportacaoJsonEnum statusImportacao;
	
	public ConciliacaoExecucaoJson(final Conciliacao model) {
		this.data = DateUtil.format(model.getData(),  DateUtil.yyyy_MM_dd);
		this.perfil = model.getPerfil().getCodigo();
		this.layout = model.getPerfil().getLayout().getCodigo();
		this.carga = model.getCarga().getId();
		this.importacao = model.getImportacao().getId();
		this.statusCarga = StatusCargaJsonEnum.fromCodigo(model.getCarga().getStatus().getCodigo());
		this.statusImportacao = StatusImportacaoJsonEnum.fromCodigo(model.getImportacao().getStatus().getCodigo());
	}
	
	public ConciliacaoExecucaoJson(final ConciliacaoForExecution model, final Date data, 
			final StatusCargaJsonEnum statusCarga, final StatusImportacaoJsonEnum statusImportacao) {
		this.data = DateUtil.format(data,  DateUtil.yyyy_MM_dd);
		this.perfil = model.getPerfil();
		this.layout = model.getLayout();
		this.carga = model.getCarga();
		this.importacao = model.getImportacao();
		this.statusCarga = statusCarga;
		this.statusImportacao = statusImportacao;
	}
}