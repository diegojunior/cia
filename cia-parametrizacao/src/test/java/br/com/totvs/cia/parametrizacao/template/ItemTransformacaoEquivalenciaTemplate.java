package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.model.Remetente;
import br.com.totvs.cia.cadastro.equivalencia.model.TipoEquivalencia;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoEquivalencia;

public class ItemTransformacaoEquivalenciaTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(TipoEquivalencia.class)
			.addTemplate("tipoMercadoEquivalencia", new Rule() {{
				add("id", "1");
				add("idLegado", "1");
				add("codigo", "TTT");
			}});

		Fixture
			.of(ItemTransformacaoEquivalencia.class)
			.addTemplate("itemEquivalenciaTipoMercadoCinf", new Rule() {{
				add("id", "2");
				add("sistema", SistemaEnum.AMPLIS);
				add("remetente", one(Remetente.class, "abn"));
				add("tipoEquivalencia", one(TipoEquivalencia.class, "tipoMercadoEquivalencia"));
			}});
		
	}

}
