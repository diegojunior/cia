package br.com.totvs.cia.importacao.template;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.importacao.model.CampoLayoutImportacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;

public class UnidadeLayoutImportacaoTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(UnidadeLayoutImportacao.class)
			.addTemplate("unidadeLayoutPosicional1", new Rule(){{
				this.add("id", "123");
				this.add("sessao", this.one(Sessao.class, "sessao32Cinf"));
				this.add("campos", UnidadeLayoutImportacaoTemplate.this.getCamposUnidade1());
			}});
		
		Fixture
			.of(UnidadeLayoutImportacao.class)
			.addTemplate("unidadeLayoutPosicional2", new Rule(){{
				this.add("id", "124");
				this.add("sessao", this.one(Sessao.class, "sessao32Cinf"));
				this.add("campos", UnidadeLayoutImportacaoTemplate.this.getCamposUnidade2());
			}});
		
		Fixture
			.of(UnidadeLayoutImportacao.class)
			.addTemplate("unidadeLayoutPosicional3", new Rule(){{
				this.add("id", "125");
				this.add("sessao", this.one(Sessao.class, "sessao32Cinf"));
				this.add("campos", UnidadeLayoutImportacaoTemplate.this.getCamposUnidade3());
			}});
		
		Fixture
			.of(UnidadeLayoutImportacao.class)
			.addTemplate("unidadeLayoutPosicional4", new Rule(){{
				this.add("id", "126");
				this.add("sessao", this.one(Sessao.class, "sessao32Cinf"));
				this.add("campos", UnidadeLayoutImportacaoTemplate.this.getCamposUnidade4());
			}});
		
		Fixture
			.of(UnidadeLayoutImportacao.class)
			.addTemplate("unidadeLayoutPosicional1", new Rule(){{
				this.add("id", "127");
				this.add("sessao", this.one(Sessao.class, "sessao32Cinf"));
				this.add("campos", UnidadeLayoutImportacaoTemplate.this.getCamposUnidade1());
			}});
		
		Fixture
			.of(UnidadeLayoutImportacao.class)
			.addTemplate("unidadeLayoutPosicionalTransformacaoCorretoraCinf", new Rule(){{
				this.add("id", "128");
				this.add("sessao", this.one(Sessao.class, "sessaoValidarDataCorretora"));
				this.add("campos", UnidadeLayoutImportacaoTemplate.this.getCamposUnidade5());
			}});
		
		
		
	}
	
	private List<CampoLayoutImportacao> getCamposUnidade1() {
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("tipoRegistroCinfPosicional1", new Rule(){{
				this.add("id", "1");
				this.add("campo", this.one(Dominio.class, "tipoRegistro"));
				this.add("valor", "32");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("bolsaCinfPosicional1", new Rule(){{
				this.add("id", "2");
				this.add("campo", this.one(Dominio.class, "bolsa"));
				this.add("valor", "BMF");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("tipoMercadoCinfPosicional1", new Rule(){{
				this.add("id", "3");
				this.add("campo", this.one(Dominio.class, "tipoMercado"));
				this.add("valor", "VST");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("cotacaoCinfPosicional", new Rule(){{
				this.add("id", "4");
				this.add("campo", this.one(Dominio.class, "cotacao"));
				this.add("valor", "0000000020000000");
				this.add("pattern", "");
			}});
		
		return Lists.newArrayList(Fixture.from(CampoLayoutImportacao.class).gimme("tipoRegistroCinfPosicional1"),
				Fixture.from(CampoLayoutImportacao.class).gimme("bolsaCinfPosicional1"),
				Fixture.from(CampoLayoutImportacao.class).gimme("tipoMercadoCinfPosicional1"),
				Fixture.from(CampoLayoutImportacao.class).gimme("cotacaoCinfPosicional"));
		
	}
	
	public List<CampoLayoutImportacao> getCamposUnidade2() {
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("tipoRegistroCinfPosicional2", new Rule(){{
				this.add("id", "5");
				this.add("campo", this.one(Dominio.class, "tipoRegistro"));
				this.add("valor", "32");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("bolsaCinfPosicional2", new Rule(){{
				this.add("id", "6");
				this.add("campo", this.one(Dominio.class, "bolsa"));
				this.add("valor", "BMF");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("tipoMercadoCinfPosicional2", new Rule(){{
				this.add("id", "7");
				this.add("campo", this.one(Dominio.class, "tipoMercado"));
				this.add("valor", "TST");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("cotacaoCinfPosicional2", new Rule(){{
				this.add("id", "8");
				this.add("campo", this.one(Dominio.class, "cotacao"));
				this.add("valor", "0000000200000000");
			}});
		return Lists.newArrayList(Fixture.from(CampoLayoutImportacao.class).gimme("tipoRegistroCinfPosicional2"),
				Fixture.from(CampoLayoutImportacao.class).gimme("bolsaCinfPosicional2"),
				Fixture.from(CampoLayoutImportacao.class).gimme("tipoMercadoCinfPosicional2"),
				Fixture.from(CampoLayoutImportacao.class).gimme("cotacaoCinfPosicional2"));
		
	}
	
	public List<CampoLayoutImportacao> getCamposUnidade3() {
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("tipoRegistroCinfPosicional3", new Rule(){{
				this.add("id", "5");
				this.add("campo", this.one(Dominio.class, "tipoRegistro"));
				this.add("valor", "32");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("bolsaCinfPosicional3", new Rule(){{
				this.add("id", "6");
				this.add("campo", this.one(Dominio.class, "bolsa"));
				this.add("valor", "TTT");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("tipoMercadoCinfPosicional3", new Rule(){{
				this.add("id", "7");
				this.add("campo", this.one(Dominio.class, "tipoMercado"));
				this.add("valor", "VST");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("cotacaoCinfPosicional3", new Rule(){{
				this.add("id", "8");
				this.add("campo", this.one(Dominio.class, "cotacao"));
				this.add("valor", "0000000020000000");
			}});
		return Lists.newArrayList(Fixture.from(CampoLayoutImportacao.class).gimme("tipoRegistroCinfPosicional3"),
				Fixture.from(CampoLayoutImportacao.class).gimme("bolsaCinfPosicional3"),
				Fixture.from(CampoLayoutImportacao.class).gimme("tipoMercadoCinfPosicional3"),
				Fixture.from(CampoLayoutImportacao.class).gimme("cotacaoCinfPosicional3"));
		
	}
	
	public List<CampoLayoutImportacao> getCamposUnidade4() {
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("tipoRegistroCinfPosicional4", new Rule(){{
				this.add("id", "5");
				this.add("campo", this.one(Dominio.class, "tipoRegistro"));
				this.add("valor", "32");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("bolsaCinfPosicional4", new Rule(){{
				this.add("id", "6");
				this.add("campo", this.one(Dominio.class, "bolsa"));
				this.add("valor", "VVV");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("tipoMercadoCinfPosicional4", new Rule(){{
				this.add("id", "7");
				this.add("campo", this.one(Dominio.class, "tipoMercado"));
				this.add("valor", "PST");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("cotacaoCinfPosicional4", new Rule(){{
				this.add("id", "8");
				this.add("campo", this.one(Dominio.class, "cotacao"));
				this.add("valor", "0000000010000000");
			}});
		return Lists.newArrayList(Fixture.from(CampoLayoutImportacao.class).gimme("tipoRegistroCinfPosicional4"),
				Fixture.from(CampoLayoutImportacao.class).gimme("bolsaCinfPosicional4"),
				Fixture.from(CampoLayoutImportacao.class).gimme("tipoMercadoCinfPosicional4"),
				Fixture.from(CampoLayoutImportacao.class).gimme("cotacaoCinfPosicional4"));
		
	}
	
	private List<CampoLayoutImportacao> getCamposUnidade5() {
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("tipoRegistroCinfPosicional5", new Rule(){{
				this.add("id", "1");
				this.add("campo", this.one(Dominio.class, "tipoRegistro"));
				this.add("valor", "32");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("corretoraCinfPosicional5", new Rule(){{
				this.add("id", "2");
				this.add("campo", this.one(Dominio.class, "corretora"));
				this.add("valor", "AGG");
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("dataCinfPosicional5", new Rule(){{
				this.add("id", "3");
				this.add("campo", this.one(Dominio.class, "data"));
				this.add("valor", "20171231");
			}});
		
		return Lists.newArrayList(Fixture.from(CampoLayoutImportacao.class).gimme("tipoRegistroCinfPosicional5"),
				Fixture.from(CampoLayoutImportacao.class).gimme("corretoraCinfPosicional5"),
				Fixture.from(CampoLayoutImportacao.class).gimme("dataCinfPosicional5"));
		
	}
	
}
