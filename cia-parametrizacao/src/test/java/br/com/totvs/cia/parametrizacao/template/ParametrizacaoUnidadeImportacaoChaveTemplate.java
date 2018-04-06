package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.CampoParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ChaveParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;

public class ParametrizacaoUnidadeImportacaoChaveTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(ParametrizacaoUnidadeImportacaoChave.class)
			.addTemplate("unidadeChave", new Rule(){{
				this.add("id", "123");
				this.add("codigo", "POS RV");
				this.add("descricao", "Parametro de unidade de posicao");
				this.add("tipoLayout", TipoLayoutEnum.XML);
				this.add("layout", this.one(LayoutXml.class, "layoutXmlComParametrizacao"));
				this.add("chaves", this.has(1).of(ChaveParametrizacaoUnidadeImportacao.class, "valid"));
				this.add("camposUnidadeImportacao", this.has(2).of(CampoParametrizacaoUnidadeImportacao.class, "cinfRptParamsFinanceiro", "cinfLftParamsPU"));
			}});
		
	}

}
