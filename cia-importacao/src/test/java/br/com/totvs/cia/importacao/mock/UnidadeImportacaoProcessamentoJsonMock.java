package br.com.totvs.cia.importacao.mock;

import br.com.six2six.fixturefactory.Fixture;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;

public class UnidadeImportacaoProcessamentoJsonMock {

	public static UnidadeImportacaoProcessamentoJson mockUnidadesCinfPosicional() {
		return Fixture
					.from(UnidadeImportacaoProcessamentoJson.class)
					.gimme("unidadeCinf1");
	}
	
	public static UnidadeImportacaoProcessamentoJson mockUnidadesTransformacao() {
		return Fixture
				.from(UnidadeImportacaoProcessamentoJson.class)
				.gimme("unidadeTransformacao");
	}
	
}
