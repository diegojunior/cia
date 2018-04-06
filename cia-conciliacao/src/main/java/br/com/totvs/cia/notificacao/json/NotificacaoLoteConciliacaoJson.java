package br.com.totvs.cia.notificacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import br.com.totvs.cia.conciliacao.json.LoteConciliacaoJson;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class NotificacaoLoteConciliacaoJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "lote")
	private LoteConciliacaoJson lote;
	
	@JsonProperty(value = "notificacoes")
	private final List<NotificacaoJson> notificacoes = Lists.newArrayList();
	
}
