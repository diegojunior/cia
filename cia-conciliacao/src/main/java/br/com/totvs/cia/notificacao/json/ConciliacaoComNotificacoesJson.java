package br.com.totvs.cia.notificacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import br.com.totvs.cia.conciliacao.json.ConciliacaoResumidaJson;
import br.com.totvs.cia.infra.json.Json;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ConciliacaoComNotificacoesJson implements Json {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "conciliacao")
	private ConciliacaoResumidaJson conciliacao;
	
	@JsonProperty(value = "notificacaoLote")
	private List<NotificacaoLoteConciliacaoJson> notificacaoLote = Lists.newArrayList();
	
	public ConciliacaoComNotificacoesJson(final ConciliacaoResumidaJson conciliacao) {
		this.conciliacao = conciliacao;
	}

}
