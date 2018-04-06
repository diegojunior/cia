package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;

public class LayoutPosicionalTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(LayoutPosicional.class)
			.addTemplate("cinfPosicional", new Rule() {{
				this.add("id", "124");
				this.add("codigo", "CINF-posicional");
				this.add("tipoLayout", TipoLayoutEnum.POSICIONAL);
				this.add("sessoes", this.has(1).of(Sessao.class, "sessao32Cinf"));
			}});
		Fixture
		.of(LayoutPosicional.class)
			.addTemplate("posicionalValidarCorretora", new Rule() {{
				this.add("id", "9999");
				this.add("codigo", "CINF-posicional");
				this.add("tipoLayout", TipoLayoutEnum.POSICIONAL);
				this.add("sessoes", this.has(1).of(Sessao.class, "sessaoValidarDataCorretora"));
			}});
		
	}

}
