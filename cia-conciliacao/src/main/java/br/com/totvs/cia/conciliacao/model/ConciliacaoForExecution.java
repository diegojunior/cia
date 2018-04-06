package br.com.totvs.cia.conciliacao.model;

import br.com.totvs.cia.infra.model.Model;

public interface ConciliacaoForExecution extends Model {
	
	String getData();
	
	String getPerfil();
	
	String getLayout();
	
	String getCarga();
	
	String getImportacao();
}