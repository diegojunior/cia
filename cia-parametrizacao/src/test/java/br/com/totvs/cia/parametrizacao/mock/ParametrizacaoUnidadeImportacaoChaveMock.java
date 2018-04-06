package br.com.totvs.cia.parametrizacao.mock;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.six2six.fixturefactory.Fixture;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;

public class ParametrizacaoUnidadeImportacaoChaveMock {

	public static List<ParametrizacaoUnidadeImportacaoChave> mock() {
		final List<ParametrizacaoUnidadeImportacaoChave> parametrizacoes = Lists.newArrayList();
		parametrizacoes.add(Fixture.from(ParametrizacaoUnidadeImportacaoChave.class).gimme("unidadeChave"));
		return parametrizacoes;
	}
}