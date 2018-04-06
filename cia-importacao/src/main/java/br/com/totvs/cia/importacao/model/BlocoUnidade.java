package br.com.totvs.cia.importacao.model;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Getter;
@Getter
public class BlocoUnidade {

	private final List<UnidadeLayoutImportacao> unidades = Lists.newArrayList();
	
	public void addUnidade(final UnidadeLayoutImportacao unidade) {
		this.unidades.add(unidade);
	}
}
