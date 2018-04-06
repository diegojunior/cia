package br.com.totvs.cia.importacao.template;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.importacao.model.CampoLayoutImportacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.importacao.model.UnidadeLoteImportacao;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;

public class UnidadeLoteImportacaoTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(UnidadeLoteImportacao.class)
			.addTemplate("unidadeCinf", new Rule() {{
				this.add("sessao", this.one(Sessao.class, "sessao32Cinf"));
				this.add("campos", UnidadeLoteImportacaoTemplate.this.getCamposUnidade());
			}});
		Fixture
			.of(UnidadeLoteImportacao.class)
			.addTemplate("unidadeTransformacaoCorretoraCinf", new Rule() {{
				this.add("sessao", this.one(Sessao.class, "sessaoValidarDataCorretora"));
				this.add("campos", UnidadeLoteImportacaoTemplate.this.getCampos());
			}});
	}
	
	private List<CampoLayoutImportacao> getCamposUnidade() {
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("tipoRegistroCinfPosicional1", new Rule(){{
				this.add("id", "1");
				this.add("campo", this.one(Dominio.class, "tipoRegistro"));
				this.add("valor", "32");
				this.add("unidade", this.one(UnidadeLayoutImportacao.class, "unidadeLayoutPosicional1"));
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("bolsaCinfPosicional1", new Rule(){{
				this.add("id", "2");
				this.add("campo", this.one(Dominio.class, "bolsa"));
				this.add("valor", "BMF");
				this.add("unidade", this.one(UnidadeLayoutImportacao.class, "unidadeLayoutPosicional1"));
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("tipoMercadoCinfPosicional1", new Rule(){{
				this.add("id", "3");
				this.add("campo", this.one(Dominio.class, "tipoMercado"));
				this.add("valor", "VST");
				this.add("unidade", this.one(UnidadeLayoutImportacao.class, "unidadeLayoutPosicional1"));
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("cotacaoCinfPosicional", new Rule(){{
				this.add("id", "4");
				this.add("campo", this.one(Dominio.class, "cotacao"));
				this.add("valor", "0000000020000000");
				this.add("pattern", "");
				this.add("unidade", this.one(UnidadeLayoutImportacao.class, "unidadeLayoutPosicional1"));
			}});
		
		return Lists.newArrayList(Fixture.from(CampoLayoutImportacao.class).gimme("tipoRegistroCinfPosicional1"),
				Fixture.from(CampoLayoutImportacao.class).gimme("bolsaCinfPosicional1"),
				Fixture.from(CampoLayoutImportacao.class).gimme("tipoMercadoCinfPosicional1"),
				Fixture.from(CampoLayoutImportacao.class).gimme("cotacaoCinfPosicional"));
		
	}
	
	private List<CampoLayoutImportacao> getCampos() {
		Fixture
		.of(CampoLayoutImportacao.class).addTemplate("tipoRegistroCinfPosicional1", new Rule(){{
			this.add("id", "1");
			this.add("campo", this.one(Dominio.class, "tipoRegistro"));
			this.add("valor", "32");
			this.add("unidade", this.one(UnidadeLayoutImportacao.class, "unidadeLayoutPosicionalTransformacaoCorretoraCinf"));
		}});
	
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("corretoraCinfPosicional1", new Rule(){{
				this.add("id", "2");
				this.add("campo", this.one(Dominio.class, "corretora"));
				this.add("valor", "AGG");
				this.add("unidade", this.one(UnidadeLayoutImportacao.class, "unidadeLayoutPosicionalTransformacaoCorretoraCinf"));
			}});
		
		Fixture
			.of(CampoLayoutImportacao.class).addTemplate("dataCinfPosicional1", new Rule(){{
				this.add("id", "3");
				this.add("campo", this.one(Dominio.class, "data"));
				this.add("valor", "20171225");
				this.add("unidade", this.one(UnidadeLayoutImportacao.class, "unidadeLayoutPosicionalTransformacaoCorretoraCinf"));
			}});
		
		return Lists.newArrayList(Fixture.from(CampoLayoutImportacao.class).gimme("tipoRegistroCinfPosicional1"),
				Fixture.from(CampoLayoutImportacao.class).gimme("corretoraCinfPosicional1"),
				Fixture.from(CampoLayoutImportacao.class).gimme("dataCinfPosicional1"));
	}

}
