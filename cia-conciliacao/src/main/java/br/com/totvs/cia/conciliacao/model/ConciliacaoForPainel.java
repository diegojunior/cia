package br.com.totvs.cia.conciliacao.model;

import br.com.totvs.cia.infra.model.Model;

public interface ConciliacaoForPainel extends Model {
	
	String getData();
	
	String getPerfil();
	
	String getLayout();
	
	String getConciliacao();
	
	String getCarga();
	
	String getImportacao();
}