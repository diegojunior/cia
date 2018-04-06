package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.LinhaBlocoParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;

public class ParametrizacaoUnidadeImportacaoBlocoTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(ParametrizacaoUnidadeImportacaoBloco.class)
			.addTemplate("unidadeImportacaoBlocoDelimitador", new Rule(){{
				this.add("id", "12345");
				this.add("codigo", "UNI IMPORT DELIMITADOR");
				this.add("tipoLayout", TipoLayoutEnum.DELIMITADOR);
				this.add("layout", this.one(LayoutDelimitador.class, "cinfDelimitador"));
				this.add("abertura", this.one(Sessao.class, "sessaoAberturaDelimitador"));
				this.add("fechamento", this.one(Sessao.class, "sessaoFechamentoDelimitador"));
				this.add("linhas", this.has(2).of(LinhaBlocoParametrizacaoUnidadeImportacao.class, "linha1", "linha2"));
			}});
		
	}

}
