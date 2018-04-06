package br.com.totvs.cia.parametrizacao.mock;

import br.com.six2six.fixturefactory.Fixture;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;

public class LayoutXmlMock {

	public static LayoutXml mockLayoutComParametrizacaoUnidade() {
		return Fixture
					.from(LayoutXml.class)
					.gimme("layoutXmlComParametrizacao");
	}
	
}
