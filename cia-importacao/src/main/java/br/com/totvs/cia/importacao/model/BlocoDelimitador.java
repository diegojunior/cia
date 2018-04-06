package br.com.totvs.cia.importacao.model;

import java.util.List;

import com.google.common.collect.Lists;

public interface BlocoDelimitador {

	public default List<UnidadeLayoutImportacao> getUnidadesLayoutImportacao() {
		return Lists.newArrayList();
	}
	
	public default List<BlocoUnidade> getBlocos() {
		return Lists.newArrayList();
	}
}
