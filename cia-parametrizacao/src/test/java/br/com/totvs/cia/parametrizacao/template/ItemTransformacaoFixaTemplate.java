package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixo;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixoDePara;

public class ItemTransformacaoFixaTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(ItemTransformacaoFixoDePara.class)
			.addTemplate("tipoMercadoCinfFixo", new Rule() {{
				add("id", "1");
				add("de", "VST");
				add("para", "TTT");
			}});

		Fixture
			.of(ItemTransformacaoFixo.class)
			.addTemplate("itemTipoMercadoCinf", new Rule() {{
				add("id", "1");
				add("itensDePara", has(1).of(ItemTransformacaoFixoDePara.class, "tipoMercadoCinfFixo"));
			}});
		
	}

}
