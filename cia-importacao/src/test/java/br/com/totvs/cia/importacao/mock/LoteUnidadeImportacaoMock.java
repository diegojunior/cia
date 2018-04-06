package br.com.totvs.cia.importacao.mock;

import br.com.six2six.fixturefactory.Fixture;
import br.com.totvs.cia.importacao.model.LoteUnidadeImportacao;

public class LoteUnidadeImportacaoMock {

	public static LoteUnidadeImportacao mockLoteCinf() {
		return Fixture
					.from(LoteUnidadeImportacao.class)
					.gimme("loteCinf");
			
	}
	
	public static LoteUnidadeImportacao mockLoteCorretoraTransformacaoCinf() {
		return Fixture
				.from(LoteUnidadeImportacao.class)
				.gimme("loteTransformacaoCorretoraCinf");
		
	}
	
}
