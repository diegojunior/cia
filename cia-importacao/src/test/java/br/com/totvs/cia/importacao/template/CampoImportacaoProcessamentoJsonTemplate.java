package br.com.totvs.cia.importacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.importacao.json.CampoImportacaoProcessamentoJson;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;

public class CampoImportacaoProcessamentoJsonTemplate implements TemplateLoader {

	@Override
	public void load() {
		Campo tipoRegistro = Fixture.from(Campo.class).gimme("tipoRegistro");
		Campo corretora = Fixture.from(Campo.class).gimme("corretoraCinf");
		Campo data = Fixture.from(Campo.class).gimme("dataCinf");
		Campo tipoMercado = Fixture.from(Campo.class).gimme("tipoMercado");
		
		Fixture
			.of(CampoImportacaoProcessamentoJson.class)
			.addTemplate("tipoRegistro", new Rule() {{
				this.add("campo", tipoRegistro.getDominio().getCodigo());
				this.add("valor", "32");
				this.add("pattern", "99");
			}});
		
		Fixture
			.of(CampoImportacaoProcessamentoJson.class)
			.addTemplate("corretoraCinf", new Rule() {{
				this.add("campo", corretora.getDominio().getCodigo());
				this.add("valor", "AGG");
			}});
		
		Fixture
			.of(CampoImportacaoProcessamentoJson.class)
			.addTemplate("tipoMercado", new Rule() {{
				this.add("campo", tipoMercado.getDominio().getCodigo());
				this.add("valor", "VST");
			}});
		
		Fixture
			.of(CampoImportacaoProcessamentoJson.class)
			.addTemplate("dataCinf", new Rule() {{
				this.add("campo", data.getDominio().getCodigo());
				this.add("valor", "2017-12-25");
				this.add("pattern", "yyyyMMdd");
			}});
		
	}

}
