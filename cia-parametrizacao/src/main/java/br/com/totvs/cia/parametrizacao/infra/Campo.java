package br.com.totvs.cia.parametrizacao.infra;

import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;

public interface Campo extends Model {

	public String getId();
	
	public Dominio getCampo();
	
	public String getValor();

	public void atualizaValor(String novoValor);
	
}
