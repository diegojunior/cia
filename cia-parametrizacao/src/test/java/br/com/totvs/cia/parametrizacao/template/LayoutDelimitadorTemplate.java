package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoDelimitadorEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;

public class LayoutDelimitadorTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(LayoutDelimitador.class)
			.addTemplate("cinfDelimitador", new Rule() {{
				this.add("id", "124");
				this.add("codigo", "CINF-delimitador");
				this.add("tipoLayout", TipoLayoutEnum.DELIMITADOR);
				this.add("tipoDelimitador", TipoDelimitadorEnum.PONTO_VIRGULA);
				this.add("sessoes", this.has(3).of(Sessao.class, "sessaoAberturaDelimitador", "sessaoPosicaoDelimitador", "sessaoFechamentoDelimitador"));
			}});
		
	}

}
