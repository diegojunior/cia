package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ChaveParametrizacaoUnidadeImportacao;

public class ChaveParametrizacaoImportacaoTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(ChaveParametrizacaoUnidadeImportacao.class)
			.addTemplate("valid", new Rule(){{
				this.add("id", "1L");
				this.add("campo", this.one(Campo.class, "codigoXml"));
				this.add("sessoes", this.has(2).of(Sessao.class, "sessaoCinfRptParams", "sessaoCinfLftParams"));
			}});
		
	}

	
}
