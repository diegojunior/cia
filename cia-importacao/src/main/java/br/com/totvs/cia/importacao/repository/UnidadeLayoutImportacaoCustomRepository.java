package br.com.totvs.cia.importacao.repository;

import java.util.List;

import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;

public interface UnidadeLayoutImportacaoCustomRepository {

	
	public List<? extends UnidadeLayoutImportacao> bulkSave(List<? extends UnidadeLayoutImportacao> entities);
	
}
