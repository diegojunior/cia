package br.com.totvs.cia.parametrizacao.mock;

import br.com.six2six.fixturefactory.Fixture;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;

public class LayoutPosicionalMock {

	public static LayoutPosicional mockLayoutCinfPosicional() {
		return Fixture
					.from(LayoutPosicional.class)
					.gimme("cinfPosicional");
	}
	
}
