package br.com.totvs.cia.notificacao.model;

import java.util.Date;

import br.com.totvs.cia.infra.model.Model;

public interface Notificacao extends Model {

	String getMensagem();
	
	Date getData();
	
	StatusEnum getStatus();
}
