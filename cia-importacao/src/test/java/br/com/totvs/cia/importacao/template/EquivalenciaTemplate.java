package br.com.totvs.cia.importacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.cadastro.equivalencia.model.Equivalencia;

public class EquivalenciaTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(Equivalencia.class)
			.addTemplate("corretora", new Rule() {{
				add("id", "1");
				add("idLegado", "1");
				add("codigoInterno", "AGORA");
				add("codigoExterno", "AGG");
			}});
		
	}

}
