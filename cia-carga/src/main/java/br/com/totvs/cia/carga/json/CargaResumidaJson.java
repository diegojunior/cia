package br.com.totvs.cia.carga.json;

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
public class CargaResumidaJson implements Json {


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
	
	public CargaResumidaJson(final Carga carga) {
		this.id = carga.getId();
		this.data = DateUtil.format(carga.getData(), DateUtil.yyyy_MM_dd);
		this.sistema = SistemaJsonEnum.fromCodigo(carga.getSistema().getCodigo());
		this.status = StatusCargaJsonEnum.fromCodigo(carga.getStatus().getCodigo());
	}
}