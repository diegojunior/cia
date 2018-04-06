package br.com.totvs.cia.notificacao.model;

import br.com.totvs.cia.conciliacao.model.Conciliacao;

public interface NotificacaoConciliacaoState {

	public void doExecute(Conciliacao conciliacao);
	
}
