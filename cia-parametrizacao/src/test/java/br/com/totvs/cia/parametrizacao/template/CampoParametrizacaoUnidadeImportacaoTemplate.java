package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.CampoParametrizacaoUnidadeImportacao;

public class CampoParametrizacaoUnidadeImportacaoTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(CampoParametrizacaoUnidadeImportacao.class)
			.addTemplate("cinfRptParamsFinanceiro", new Rule(){{
				this.add("id", "123");
				this.add("sessao", this.one(Sessao.class, "sessaoCinfRptParams"));
				this.add("campo",this.one(Campo.class, "financeiroXml"));
			}});
		
		Fixture
			.of(CampoParametrizacaoUnidadeImportacao.class)
			.addTemplate("cinfLftParamsPU", new Rule(){{
				this.add("id", "321");
				this.add("sessao", this.one(Sessao.class, "sessaoCinfLftParams"));
				this.add("campo", this.one(Campo.class, "puXml"));
			}});
		
	}

}
