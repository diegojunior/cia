package br.com.totvs.cia.parametrizacao.template;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.CampoValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.LocalValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.TipoValidacaoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.ValidacaoArquivoInterno;

public class ValidacaoInternaTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(ValidacaoArquivoInterno.class)
			.addTemplate("validacaoCorretora", new Rule() {{
				this.add("tipoValidacao", TipoValidacaoEnum.ARQUIVO);
				this.add("tipoLayout", TipoLayoutEnum.POSICIONAL);
				this.add("layout", this.one(LayoutPosicional.class, "posicionalValidarCorretora"));
				this.add("localValidacao", LocalValidacaoArquivoEnum.INTERNO);
				this.add("campoValidacao", CampoValidacaoArquivoEnum.CORRETORA);
				this.add("sessaoLayout", this.one(Sessao.class, "sessaoValidarDataCorretora"));
				this.add("campoLayout", this.one(Campo.class, "corretoraCinf"));
			}});
		
		Fixture
			.of(ValidacaoArquivoInterno.class)
			.addTemplate("validacaoData", new Rule() {{
				this.add("tipoValidacao", TipoValidacaoEnum.ARQUIVO);
				this.add("tipoLayout", TipoLayoutEnum.POSICIONAL);
				this.add("layout", this.one(LayoutPosicional.class, "posicionalValidarCorretora"));
				this.add("localValidacao", LocalValidacaoArquivoEnum.INTERNO);
				this.add("campoValidacao", CampoValidacaoArquivoEnum.DATA);
				this.add("sessaoLayout", this.one(Sessao.class, "sessaoValidarDataCorretora"));
				this.add("campoLayout", this.one(Campo.class, "dataCinf"));
			}});
		
	}

}
