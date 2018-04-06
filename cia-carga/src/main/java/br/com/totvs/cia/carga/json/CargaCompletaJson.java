package br.com.totvs.cia.carga.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumDeserializer;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumSerializer;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.infra.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargaCompletaJson implements Json {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String data;

	@JsonProperty("sistema")
	@JsonSerialize(using = SistemaJsonEnumSerializer.class)
	@JsonDeserialize(using = SistemaJsonEnumDeserializer.class)
	private SistemaJsonEnum sistema;

	@JsonProperty("status")
	@JsonSerialize(using = StatusCargaJsonEnumSerializer.class)
	@JsonDeserialize(using = StatusCargaJsonEnumDesirializer.class)
	private StatusCargaJsonEnum status;
	
	private List<LoteCargaJson> lotes;
	
	public CargaCompletaJson(final Carga model, final List<LoteCargaJson> lotesJson) {
		this.id = model.getId();
		this.data = DateUtil.format(model.getData(), DateUtil.yyyy_MM_dd);
		this.sistema = SistemaJsonEnum.fromCodigo(model.getSistema().getCodigo());
		this.status = StatusCargaJsonEnum.fromCodigo(model.getStatus().getCodigo());
		this.lotes = lotesJson;
	}
}