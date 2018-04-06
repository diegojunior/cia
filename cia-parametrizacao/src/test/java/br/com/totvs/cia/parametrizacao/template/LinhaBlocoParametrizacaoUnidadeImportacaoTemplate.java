package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.LinhaBlocoParametrizacaoUnidadeImportacao;

public class LinhaBlocoParametrizacaoUnidadeImportacaoTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(LinhaBlocoParametrizacaoUnidadeImportacao.class)
			.addTemplate("linha1", new Rule(){{
				this.add("id", "123456");
				this.add("sessao", this.one(Sessao.class, "sessaoAberturaDelimitador"));
				this.add("campos", this.has(1).of(Campo.class, "carteiraCinfDelimitador"));
			}});
		Fixture
			.of(LinhaBlocoParametrizacaoUnidadeImportacao.class)
			.addTemplate("linha2", new Rule(){{
				this.add("id", "654321");
				this.add("sessao", this.one(Sessao.class, "sessaoPosicaoDelimitador"));
				this.add("campos", this.has(2).of(Campo.class, "quantidadeDelimitador", "valorBrutoDelimitador"));
			}});

	}

}
