package br.com.totvs.cia.parametrizacao.mock;

import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;

public class CampoMock {
	
	public static Campo mock(final String id, final Dominio dominio, final int posicaoInicial, final int posicaoFinal, final int tamanho,
			final String tag, final int ordem) {
		Campo campo = new Campo();
		campo.setId(id);
		campo.setDominio(dominio);
		campo.setDescricao("teste");
		campo.setTag(tag);
		campo.setPosicaoInicial(posicaoInicial);
		campo.setPosicaoFinal(posicaoFinal);
		campo.setOrdem(ordem);
		campo.setTamanho(tamanho);

		return campo;
	}
	
	public static Campo mockComPattern(final String id, final Dominio dominio, final int posicaoInicial, final int posicaoFinal, final int tamanho,
			final String pattern, final int ordem) {
		Campo campo = new Campo();
		campo.setId(id);
		campo.setDominio(dominio);
		campo.setDescricao("teste");
		campo.setPattern(pattern);
		campo.setPosicaoInicial(posicaoInicial);
		campo.setPosicaoFinal(posicaoFinal);
		campo.setOrdem(ordem);
		campo.setTamanho(tamanho);
		
		return campo;
	}

}
