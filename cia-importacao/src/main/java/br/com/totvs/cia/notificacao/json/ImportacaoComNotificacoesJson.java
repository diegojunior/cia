package br.com.totvs.cia.notificacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import br.com.totvs.cia.importacao.json.ImportacaoJson;
import br.com.totvs.cia.infra.json.Json;
import lombok.Data;
@Data
public class ImportacaoComNotificacoesJson implements Json {

	private static final long serialVersionUID = 1L;
	
	@JsonProperty(value = "importacao")
	private ImportacaoJson importacao;
	
	@JsonProperty(value = "notificacoes")
	private final List<NotificacaoJson> notificacoes = Lists.newArrayList();
	
	public ImportacaoComNotificacoesJson(final ImportacaoJson importacao) {
		this.importacao = importacao;
	}

}
