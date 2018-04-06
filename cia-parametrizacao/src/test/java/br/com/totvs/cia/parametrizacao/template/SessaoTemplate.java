package br.com.totvs.cia.parametrizacao.template;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.six2six.fixturefactory.loader.TemplateLoader;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;

public class SessaoTemplate implements TemplateLoader {

	@Override
	public void load() {
		Fixture
			.of(Sessao.class)
			.addTemplate("sessao32Cinf", new Rule(){{
				this.add("codigo", "32");
				this.add("campos", this.has(4).of(Campo.class, "tipoRegistroCinf", "bolsaCinf", "tipoMercadoCinf", "cotacaoCinf"));
			}});
		
		Fixture
			.of(Sessao.class)
			.addTemplate("sessaoAberturaDelimitador", new Rule(){{
				this.add("nome", "header");
				this.add("codigo", "1");
				this.add("campos", this.has(2).of(Campo.class, "tipoRegistroCinfDelimitador", "carteiraCinfDelimitador"));
			}});
		
		Fixture
			.of(Sessao.class)
			.addTemplate("sessaoPosicaoDelimitador", new Rule(){{
				this.add("nome", "posicao");
				this.add("codigo", "2");
				this.add("campos", this.has(3).of(Campo.class, "tipoRegistroCinfDelimitador", "quantidadeDelimitador", "valorBrutoDelimitador"));
			}});
		
		Fixture
			.of(Sessao.class)
			.addTemplate("sessaoFechamentoDelimitador", new Rule(){{
				this.add("nome", "footer");
				this.add("codigo", "3");
				this.add("campos", this.has(2).of(Campo.class, "tipoRegistroCinfDelimitador", "totalDelimitador"));
			}});
		
		Fixture
			.of(Sessao.class)
			.addTemplate("sessaoCinfRptParams", new Rule(){{
				this.add("nome", "RptParams");
				this.add("codigo", "/Document/BizFileHdr/Xchg/BizGrp/Document/SttlmRpt/RptParams");
				this.add("campos", SessaoTemplate.this.getCamposSessaoCinfXmlRptParams());
			}});
		Fixture
			.of(Sessao.class)
			.addTemplate("sessaoCinfLftParams", new Rule(){{
				this.add("nome", "LftParams");
				this.add("codigo", "/Document/BizFileHdr/Xchg/BizGrp/Document/SttlmRpt/LftParams");
				this.add("campos", SessaoTemplate.this.getCamposSessaoCinfXmlLftParams());
			}});
		
		Fixture
			.of(Sessao.class)
			.addTemplate("sessaoValidarDataCorretora", new Rule() {{
				this.add("nome", "sessaoDataCorretora");
				this.add("codigo", "32");
				this.add("campos", this.has(3).of(Campo.class, "tipoRegistro", "corretoraCinf", "dataCinf"));
			}});
		
		Fixture
			.of(Sessao.class)
			.addTemplate("sessao32Transformacao", new Rule(){{
				this.add("nome", "sessao32");
				this.add("codigo", "32");
				this.add("campos", this.has(3).of(Campo.class, "tipoRegistro", "tipoMercado", "corretora"));
			}});
			
	}
	
	public List<Campo> getCamposSessaoCinfXmlRptParams() {
		List<Campo> campos = Lists.newArrayList();
		campos.add(Fixture.from(Campo.class).gimme("codigoXml"));
		campos.add(Fixture.from(Campo.class).gimme("flagXml"));
		campos.add(Fixture.from(Campo.class).gimme("financeiroXml"));
		campos.add(Fixture.from(Campo.class).gimme("dataXml"));
		campos.add(Fixture.from(Campo.class).gimme("quantidadeXml"));
		campos.add(Fixture.from(Campo.class).gimme("valorXml"));
		return campos;
	}
	
	protected List<Campo> getCamposSessaoCinfXmlLftParams() {
		List<Campo> campos = Lists.newArrayList();
		campos.add(Fixture.from(Campo.class).gimme("codigoLftParamsXml"));
		campos.add(Fixture.from(Campo.class).gimme("puXml"));
		return campos;
	}

}
