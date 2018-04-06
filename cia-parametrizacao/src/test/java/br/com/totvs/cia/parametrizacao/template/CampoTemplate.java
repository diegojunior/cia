package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;

public class CampoTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(Campo.class)
			.addTemplate("tipoRegistro", new Rule() {{
				this.add("id", "1");
				this.add("dominio", this.one(Dominio.class, "tipoRegistro"));
				this.add("descricao", "tipo de registro");
				this.add("ordem", 1);
				this.add("pattern", "9");
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("carteira", new Rule() {{
				this.add("id", "2");
				this.add("dominio", this.one(Dominio.class, "carteira"));
				this.add("ordem", 1);
				this.add("descricao", "carteira");
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("ativo", new Rule() {{
				this.add("id", "3");
				this.add("dominio", this.one(Dominio.class, "ativo"));
				this.add("ordem", 1);
				this.add("descricao", "ativo");
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("corretora", new Rule() {{
				this.add("id", "4");
				this.add("dominio", this.one(Dominio.class, "corretora"));
				this.add("ordem", 1);
				this.add("descricao", "corretora");
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("quantidade", new Rule() {{
				this.add("id", "5");
				this.add("dominio", this.one(Dominio.class, "quantidade"));
				this.add("ordem", 1);
				this.add("descricao", "quantidade");
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("valorBruto", new Rule() {{
				this.add("id", "6");
				this.add("dominio", this.one(Dominio.class, "valorBruto"));
				this.add("ordem", 1);
				this.add("descricao", "valorBruto");
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("total", new Rule() {{
				this.add("id", "7");
				this.add("dominio", this.one(Dominio.class, "total"));
				this.add("ordem", 1);
				this.add("descricao", "total");
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("bolsa", new Rule() {{
				this.add("id", "8");
				this.add("dominio", this.one(Dominio.class, "bolsa"));
				this.add("ordem", 1);
				this.add("descricao", "bolsa");
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("tipoMercado", new Rule() {{
				this.add("id", "9");
				this.add("dominio", this.one(Dominio.class, "tipoMercado"));
				this.add("ordem", 1);
				this.add("descricao", "tipoMercado");
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("cotacao", new Rule() {{
				this.add("id", "10");
				this.add("dominio", this.one(Dominio.class, "cotacao"));
				this.add("ordem", 1);
				this.add("descricao", "cotacao");
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("tipoRegistroCinf")
			.inherits("tipoRegistro", new Rule() {{
				this.add("posicaoInicial", 1);
				this.add("posicaoFinal", 2);
				this.add("tamanho", 2);
				this.add("ordem", 1);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("bolsaCinf")
			.inherits("bolsa", new Rule() {{
				this.add("posicaoInicial", 3);
				this.add("posicaoFinal", 5);
				this.add("tamanho", 3);
				this.add("ordem", 2);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("tipoMercadoCinf")
			.inherits("tipoMercado", new Rule() {{
				this.add("posicaoInicial", 6);
				this.add("posicaoFinal", 8);
				this.add("tamanho", 3);
				this.add("ordem", 3);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("cotacaoCinf")
			.inherits("cotacao", new Rule() {{
				this.add("posicaoInicial", 9);
				this.add("posicaoFinal", 24);
				this.add("tamanho", 15);
				this.add("pattern", "999999999999.999");
				this.add("ordem", 4);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("tipoRegistroCinfDelimitador")
			.inherits("tipoRegistro", new Rule() {{
				this.add("ordem", 1);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("carteiraCinfDelimitador")
			.inherits("carteira", new Rule() {{
				this.add("ordem", 2);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("quantidadeDelimitador")
			.inherits("quantidade", new Rule(){{
				this.add("ordem", 2);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("valorBrutoDelimitador")
			.inherits("valorBruto", new Rule(){{
				this.add("ordem", 3);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("totalDelimitador")
			.inherits("total", new Rule(){{
				this.add("ordem", 2);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("codigoXml", new Rule() {{
				this.add("id", "11");
				this.add("dominio", this.one(Dominio.class, "codigo"));
				this.add("tag", "/Frqcy/xpto/RptNb");
				this.add("ordem", 1);
			}});
	
		Fixture
			.of(Campo.class)
			.addTemplate("flagXml", new Rule() {{
				this.add("id", "12");
				this.add("dominio", this.one(Dominio.class, "flag"));
				this.add("tag", "/Frqcy/xpto/ActvtyInd");
				this.add("ordem", 2);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("financeiroXml", new Rule() {{
				this.add("id", "13");
				this.add("dominio", this.one(Dominio.class, "financeiro"));
				this.add("tag", "/Frqcy/xpto/Financeiro");
				this.add("pattern", "999999.99");
				this.add("ordem", 3);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("dataXml", new Rule() {{
				this.add("id", "14");
				this.add("dominio", this.one(Dominio.class, "data"));
				this.add("tag", "/Frqcy/xpto/Data");
				this.add("pattern", "yyyyMMdd");
				this.add("ordem", 4);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("quantidadeXml", new Rule() {{
				this.add("id", "15");
				this.add("dominio", this.one(Dominio.class, "quantidade"));
				this.add("tag", "/Frqcy/xpto/Quantidade");
				this.add("pattern", "999");
				this.add("ordem", 5);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("valorXml", new Rule() {{
				this.add("id", "16");
				this.add("dominio", this.one(Dominio.class, "valorBruto"));
				this.add("tag", "/Frqcy/xpto/ValorLiquidacao/@valor");
				this.add("pattern", "99");
				this.add("ordem", 6);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("codigoLftParamsXml", new Rule() {{
				this.add("id", "17");
				this.add("dominio", this.one(Dominio.class, "codigo"));
				this.add("tag", "/RptNb");
				this.add("ordem", 1);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("puXml", new Rule() {{
				this.add("id", "18");
				this.add("dominio", this.one(Dominio.class, "pu"));
				this.add("tag", "/Pu");
				this.add("pattern", "999");
				this.add("ordem", 2);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("corretoraCinf")
			.inherits("corretora", new Rule(){{
				this.add("ordem", 2);
			}});
		
		Fixture
			.of(Campo.class)
			.addTemplate("dataCinf", new Rule() {{
				this.add("id", "19");
				this.add("dominio", this.one(Dominio.class, "data"));
				this.add("pattern", "yyyyMMdd");
				this.add("ordem", 3);
			}});
		
	}

}
