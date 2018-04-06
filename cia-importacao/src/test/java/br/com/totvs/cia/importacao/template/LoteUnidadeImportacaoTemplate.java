package br.com.totvs.cia.importacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.importacao.model.LoteUnidadeImportacao;
import br.com.totvs.cia.importacao.model.UnidadeLoteImportacao;

public class LoteUnidadeImportacaoTemplate implements TemplateLoader{

	@Override
	public void load() {
		Fixture
			.of(LoteUnidadeImportacao.class)
			.addTemplate("loteCinf", new Rule() {{
				add("unidades", has(1).of(UnidadeLoteImportacao.class, "unidadeCinf"));
			}});
		Fixture
			.of(LoteUnidadeImportacao.class)
			.addTemplate("loteTransformacaoCorretoraCinf", new Rule() {{
				add("unidades", has(1).of(UnidadeLoteImportacao.class, "unidadeTransformacaoCorretoraCinf"));
			}});
		
	}

}
