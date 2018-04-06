package br.com.totvs.cia.notificacao.json;

import static br.com.totvs.cia.notificacao.json.StatusEnumJson.fromCodigo;

import br.com.totvs.cia.notificacao.model.NotificacaoConciliacao;
public class NotificacaoConciliacaoJson extends NotificacaoJson {
	
	private static final long serialVersionUID = 1L;
	
	public NotificacaoConciliacaoJson(final NotificacaoConciliacao model) {
		super(model.getId(), model.getData(), fromCodigo(model.getStatus().getCodigo()), model.getMensagem());
	}
}
