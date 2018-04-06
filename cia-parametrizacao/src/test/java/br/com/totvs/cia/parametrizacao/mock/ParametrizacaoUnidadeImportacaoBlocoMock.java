package br.com.totvs.cia.parametrizacao.mock;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.six2six.fixturefactory.Fixture;
import br.com.totvs.cia.parametrizacao.template.ParametrizacaoUnidadeImportacaoBlocoTemplate;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;

public class ParametrizacaoUnidadeImportacaoBlocoMock {

	public static List<ParametrizacaoUnidadeImportacaoBloco> mockParametrizacaoDelimitador() {
		new ParametrizacaoUnidadeImportacaoBlocoTemplate().load();
		List<ParametrizacaoUnidadeImportacaoBloco> params = Lists.newArrayList();
		params.add(Fixture.from(ParametrizacaoUnidadeImportacaoBloco.class).gimme("unidadeImportacaoBlocoDelimitador"));
		return params;
	}

}
