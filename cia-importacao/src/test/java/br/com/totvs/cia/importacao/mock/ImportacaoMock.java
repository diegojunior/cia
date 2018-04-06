package br.com.totvs.cia.importacao.mock;

import br.com.six2six.fixturefactory.Fixture;
import br.com.totvs.cia.importacao.model.Importacao;

public class ImportacaoMock {

	public static Importacao mockPosicionalCinf() {
		
		return Fixture.from(Importacao.class).gimme("importacaoCinf");
	}
	
	public static Importacao mockPosicionalCinfTamanhoInvalido() {
		
		return Fixture.from(Importacao.class).gimme("importacaoCinfTamanhoArquivoInvalido");
	}
	
	public static Importacao mockDelimitadorPontoVirgula() {
		
		return Fixture.from(Importacao.class).gimme("importacaoDelimitadorPontoVirgula");
	}
	
	public static Importacao mockXml() {
		
		return Fixture.from(Importacao.class).gimme("importacaoXml");
	}
	
	public static Importacao mockXmlComParametrizacaoUnidade() {
		
		return Fixture.from(Importacao.class).gimme("importacaoXmlComParametrizacao");
	}
	
	public static Importacao mockPosicionalCinfComValidacaoCorretora() {
		
		return Fixture.from(Importacao.class).gimme("importacaoCinfComValidacaoCorretora");
	}
	
	public static Importacao mockPosicionalCinfComTransformacaoEquivalencia() {
		
		return Fixture.from(Importacao.class).gimme("importacaoCinfComTransformacaoEquivalencia");
	}
	
}
