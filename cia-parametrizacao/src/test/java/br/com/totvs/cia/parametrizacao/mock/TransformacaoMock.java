package br.com.totvs.cia.parametrizacao.mock;

import br.com.six2six.fixturefactory.Fixture;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;

public class TransformacaoMock {

	public static Transformacao mockTransformacaoFixaTipoMercadoCinf() {
		
		return Fixture
				.from(Transformacao.class)
				.gimme("transformacaoFixaTipoMercadoCinf");
	}
	
	public static Transformacao mockTransformacaoEquivalenciaTipoMercadoCinf() {
		
		return Fixture
				.from(Transformacao.class)
				.gimme("transformacaoEquivalenciaTipoMercadoCinf");
	}
	
}
