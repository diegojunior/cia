package br.com.totvs.cia.notificacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import br.com.totvs.cia.carga.json.CargaResumidaJson;
import br.com.totvs.cia.carga.json.NotificacaoLoteCargaJson;
import br.com.totvs.cia.infra.json.Json;
import lombok.Data;
@Data
public class CargaComNotificacoesJson implements Json {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "carga")
	private CargaResumidaJson carga;
	
	@JsonProperty(value = "notificacaoLote")
	private List<NotificacaoLoteCargaJson> lotesCarga = Lists.newArrayList();
	
	public CargaComNotificacoesJson(final CargaResumidaJson carga) {
		this.carga = carga;
	}

}
