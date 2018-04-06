package br.com.totvs.cia.parametrizacao.mock;

import br.com.six2six.fixturefactory.Fixture;
import br.com.totvs.cia.parametrizacao.validacao.model.ValidacaoArquivoInterno;

public class ValidacaoInternaMock {

	public static ValidacaoArquivoInterno mockValidacaoInternaCorretora() {
		return Fixture
					.from(ValidacaoArquivoInterno.class)
					.gimme("validacaoCorretora");
	}
	
	public static ValidacaoArquivoInterno mockValidacaoInternaData() {
		return Fixture
				.from(ValidacaoArquivoInterno.class)
				.gimme("validacaoData");
	}
	
}
