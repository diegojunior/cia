package br.com.totvs.cia.importacao.repository;

import java.util.List;

import br.com.totvs.cia.importacao.model.UnidadeImportacao;

public interface UnidadeImportacaoCustomRepository {

	public List<? extends UnidadeImportacao> bulkSave(List<? extends UnidadeImportacao> entities);
	
}
