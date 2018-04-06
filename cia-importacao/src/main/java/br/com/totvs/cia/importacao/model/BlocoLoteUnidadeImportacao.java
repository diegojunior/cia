package br.com.totvs.cia.importacao.model;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Getter;
@Getter
public class BlocoLoteUnidadeImportacao implements BlocoDelimitador {
	
	private final List<BlocoUnidade> blocos = Lists.newArrayList();
	
	public void addBlocoUnidades(final BlocoUnidade bloco) {
		this.blocos.add(bloco);
	}

}
