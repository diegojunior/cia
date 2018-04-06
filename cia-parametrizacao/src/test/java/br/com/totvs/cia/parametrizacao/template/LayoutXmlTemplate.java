package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;

public class LayoutXmlTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(LayoutXml.class)
			.addTemplate("cinfXml", new Rule() {{
				this.add("id", "125");
				this.add("codigo", "CINF-xml");
				this.add("tipoLayout", TipoLayoutEnum.XML);
				this.add("sessoes", this.has(2).of(Sessao.class, "sessaoCinfRptParams", "sessaoCinfLftParams"));
			}});
		
		Fixture
			.of(LayoutXml.class)
			.addTemplate("layoutXmlComParametrizacao", new Rule() {{
				this.add("id", "126");
				this.add("codigo", "CINF-xml-parametrizacao");
				this.add("tipoLayout", TipoLayoutEnum.XML);
				this.add("sessoes", this.has(2).of(Sessao.class, "sessaoCinfRptParams", "sessaoCinfLftParams"));
			}});
		
	}

}
