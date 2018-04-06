package br.com.totvs.cia.conciliacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.PerfilConciliacaoJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonRootName(value = "Conciliacao")
public class ConciliacaoResumidaJson implements Json {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty("id")
	private String id;
	
	@JsonProperty("data")
	private String data;
	
	@JsonProperty("perfil")
	private PerfilConciliacaoJson perfil;
	
	@JsonProperty("status")
	@JsonSerialize(using = StatusConciliacaoJsonEnumSerializer.class)
	@JsonDeserialize(using = StatusConciliacaoJsonEnumDeserializer.class)
	private StatusConciliacaoJsonEnum status;
	
	public ConciliacaoResumidaJson(final Conciliacao model, final PerfilConciliacaoJson perfil) {
		this.id = model.getId();
		this.data = DateUtil.format(model.getData(), DateUtil.yyyy_MM_dd);
		this.perfil = perfil;
		this.status = model.getStatus() != null ? StatusConciliacaoJsonEnum.fromCodigo(model.getStatus().getCodigo()) : null;
	}
}