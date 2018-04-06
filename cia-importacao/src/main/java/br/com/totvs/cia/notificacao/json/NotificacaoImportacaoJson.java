package br.com.totvs.cia.notificacao.json;

import static br.com.totvs.cia.notificacao.json.StatusEnumJson.fromCodigo;

import br.com.totvs.cia.notificacao.model.NotificacaoImportacao;
public class NotificacaoImportacaoJson extends NotificacaoJson {
	
	private static final long serialVersionUID = 1L;
	
	public NotificacaoImportacaoJson(final NotificacaoImportacao model) {
		super(model.getId(), model.getData(), fromCodigo(model.getStatus().getCodigo()), model.getMensagem());
	}
	
}
