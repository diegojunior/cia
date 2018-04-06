package br.com.totvs.cia.importacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.cadastro.equivalencia.model.Remetente;

public class RemetenteTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(Remetente.class)
			.addTemplate("abn", new Rule() {{
				add("id", "1");
				add("idLegado", "1");
				add("codigo", "abn");
			}});
		
	}

}
