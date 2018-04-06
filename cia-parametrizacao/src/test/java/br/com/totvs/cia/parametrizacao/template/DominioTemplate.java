package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.cadastro.tipo.TipoValorDominioEnum;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;

public class DominioTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(Dominio.class)
			.addTemplate("tipoRegistro", new Rule(){{
				this.add("codigo", "Tipo Registro");
				this.add("tipo", TipoValorDominioEnum.NUMERICO);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("carteira", new Rule(){{
				this.add("codigo", "Carteira");
				this.add("tipo", TipoValorDominioEnum.ALFANUMERICO);
			}});
		
		Fixture
		.of(Dominio.class)
		.addTemplate("ativo", new Rule(){{
			this.add("codigo", "Ativo");
			this.add("tipo", TipoValorDominioEnum.ALFANUMERICO);
		}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("corretora", new Rule(){{
				this.add("codigo", "Corretora");
				this.add("tipo", TipoValorDominioEnum.ALFANUMERICO);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("quantidade", new Rule(){{
				this.add("codigo", "Quantidade");
				this.add("tipo", TipoValorDominioEnum.NUMERICO);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("valorBruto", new Rule(){{
				this.add("codigo", "Valor Bruto");
				this.add("tipo", TipoValorDominioEnum.DECIMAL);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("total", new Rule(){{
				this.add("codigo", "Total");
				this.add("tipo", TipoValorDominioEnum.NUMERICO);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("bolsa", new Rule(){{
				this.add("codigo", "Bolsa");
				this.add("tipo", TipoValorDominioEnum.ALFANUMERICO);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("tipoMercado", new Rule(){{
				this.add("codigo", "Tipo Mercado");
				this.add("tipo", TipoValorDominioEnum.ALFANUMERICO);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("cotacao", new Rule(){{
				this.add("codigo", "Cotacao");
				this.add("tipo", TipoValorDominioEnum.NUMERICO);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("codigo", new Rule(){{
				this.add("codigo", "Código");
				this.add("tipo", TipoValorDominioEnum.ALFANUMERICO);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("flag", new Rule(){{
				this.add("codigo", "Flag");
				this.add("tipo", TipoValorDominioEnum.ALFANUMERICO);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("financeiro", new Rule(){{
				this.add("codigo", "Financeiro");
				this.add("tipo", TipoValorDominioEnum.ALFANUMERICO);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("data", new Rule(){{
				this.add("codigo", "Data");
				this.add("tipo", TipoValorDominioEnum.DATA);
			}});
		
		Fixture
			.of(Dominio.class)
			.addTemplate("pu", new Rule(){{
				this.add("codigo", "PU");
				this.add("tipo", TipoValorDominioEnum.NUMERICO);
			}});
		
	}

}
