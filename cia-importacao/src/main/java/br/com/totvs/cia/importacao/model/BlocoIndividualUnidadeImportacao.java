package br.com.totvs.cia.importacao.model;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Getter;
@Getter
public class BlocoIndividualUnidadeImportacao implements BlocoDelimitador {
	
	private final List<UnidadeLayoutImportacao> unidadesLayoutImportacao = Lists.newArrayList();
	
	public void addUnidadeLayoutImportacao(final UnidadeLayoutImportacao unidade) {
		this.unidadesLayoutImportacao.add(unidade);
	}

}
