package br.com.totvs.cia.importacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.importacao.json.CampoImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;

public class UnidadeImportacaoProcessamentoJsonTemplate implements TemplateLoader {

	@Override
	public void load() {
		Sessao sessao = Fixture.from(Sessao.class).gimme("sessaoValidarDataCorretora");
		Fixture
			.of(UnidadeImportacaoProcessamentoJson.class)
			.addTemplate("unidadeCinf1", new Rule() {{
				this.add("idSessao", sessao.getId());
				this.add("codigoSessao", sessao.getCodigo());
				this.add("numeroLinha", 1);
				this.add("numeroLinhaAnterior", 0);
				this.add("campos", this.has(3).of(CampoImportacaoProcessamentoJson.class, "tipoRegistro", "corretoraCinf", "dataCinf"));
				
			}});
		Fixture
			.of(UnidadeImportacaoProcessamentoJson.class)
			.addTemplate("unidadeTransformacao", new Rule() {{
				this.add("idSessao", sessao.getId());
				this.add("codigoSessao", sessao.getCodigo());
				this.add("numeroLinha", 1);
				this.add("numeroLinhaAnterior", 0);
				this.add("campos", this.has(3).of(CampoImportacaoProcessamentoJson.class, "tipoRegistro", "corretoraCinf", "tipoMercado"));
				
			}});
		
	}

}
