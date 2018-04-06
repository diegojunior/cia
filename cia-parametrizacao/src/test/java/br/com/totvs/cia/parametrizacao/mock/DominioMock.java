package br.com.totvs.cia.parametrizacao.mock;

import java.util.List;

import br.com.six2six.fixturefactory.Fixture;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.template.DominioTemplate;

public class DominioMock {
	
	static  {
		new DominioTemplate().load();
	}

	public static List<Dominio> allMocks() {
		return Fixture
				.from(Dominio.class)
				.gimme(15, "tipoRegistro",
						"carteira", 
						"ativo", 
						"corretora", 
						"quantidade", 
						"valorBruto", 
						"total", 
						"bolsa", 
						"tipoMercado", 
						"cotacao",
						"codigo",
						"flag",
						"financeiro", 
						"data",
						"pu");
	}
}
