package br.com.totvs.cia.conciliacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ConciliacaoCompletaJson implements Json {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String data;
	
	private PerfilConciliacaoJson perfil;
	
	@JsonProperty("status")
	@JsonSerialize(using = StatusConciliacaoJsonEnumSerializer.class)
	@JsonDeserialize(using = StatusConciliacaoJsonEnumDeserializer.class)
	private StatusConciliacaoJsonEnum status;
	
	private LoteConciliacaoJson lote;
	
	public ConciliacaoCompletaJson(final Conciliacao model, final PerfilConciliacaoJson perfil, final LoteConciliacaoJson lote) {
		this.id = model.getId();
		this.data = DateUtil.format(model.getData(), DateUtil.yyyy_MM_dd);
		this.perfil = perfil;
		this.status = StatusConciliacaoJsonEnum.fromCodigo(model.getStatus().getCodigo());
		this.lote = lote;
	}
}