package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoEquivalencia;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixo;
import br.com.totvs.cia.parametrizacao.transformacao.model.TipoTransformacaoEnum;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;

public class TransformacaoTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(Transformacao.class)
			.addTemplate("transformacaoFixaTipoMercadoCinf", new Rule() {{
				add("id", "1");
				add("tipoLayout", TipoLayoutEnum.POSICIONAL);
				add("layout", one(LayoutPosicional.class, "cinfPosicional"));
				add("sessao", one(Sessao.class, "sessao32Cinf"));
				add("campo", one(Campo.class, "tipoMercadoCinf"));
				add("tipoTransformacao", TipoTransformacaoEnum.FIXO);
				add("item", one(ItemTransformacaoFixo.class, "itemTipoMercadoCinf"));
			}});
		Fixture
			.of(Transformacao.class)
			.addTemplate("transformacaoEquivalenciaTipoMercadoCinf", new Rule() {{
				add("id", "2");
				add("tipoLayout", TipoLayoutEnum.POSICIONAL);
				add("layout", one(LayoutPosicional.class, "cinfPosicional"));
				add("sessao", one(Sessao.class, "sessaoValidarDataCorretora"));
				add("campo", one(Campo.class, "corretoraCinf"));
				add("tipoTransformacao", TipoTransformacaoEnum.EQUIVALENCIA);
				add("item", one(ItemTransformacaoEquivalencia.class, "itemEquivalenciaTipoMercadoCinf"));
			}});
		
	}

}
