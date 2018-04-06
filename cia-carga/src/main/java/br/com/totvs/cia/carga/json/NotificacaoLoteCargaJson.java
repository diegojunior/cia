package br.com.totvs.cia.carga.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.notificacao.json.NotificacaoJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificacaoLoteCargaJson implements Json, Comparable<NotificacaoLoteCargaJson>{

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "lote")
	private LoteCargaJson lote;
	
	@JsonProperty(value = "notificacoes")
	private final List<NotificacaoJson> notificacoes = Lists.newArrayList();

	@Override
	public int compareTo(final NotificacaoLoteCargaJson outro) {
		return this.getLote().getServico().getCodigo().compareTo(outro.getLote().getServico().getCodigo());
	}
}