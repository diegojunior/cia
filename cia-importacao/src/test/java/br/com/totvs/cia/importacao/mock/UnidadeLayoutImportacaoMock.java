package br.com.totvs.cia.importacao.mock;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.Rule;
import br.com.totvs.cia.importacao.model.CampoLayoutImportacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.template.DominioTemplate;

public class UnidadeLayoutImportacaoMock {

	public static List<UnidadeLayoutImportacao> mockUnidadesImportacaoCinfPosicional() {
		final List<UnidadeLayoutImportacao> unidades = Lists.newArrayList();
		unidades.add(Fixture.from(UnidadeLayoutImportacao.class).gimme("unidadeLayoutPosicional1"));
		unidades.add(Fixture.from(UnidadeLayoutImportacao.class).gimme("unidadeLayoutPosicional2"));
		unidades.add(Fixture.from(UnidadeLayoutImportacao.class).gimme("unidadeLayoutPosicional3"));
		unidades.add(Fixture.from(UnidadeLayoutImportacao.class).gimme("unidadeLayoutPosicional4"));
		
		return unidades;
	}
	
	public static List<UnidadeLayoutImportacao> mockUnidadesImportacaoDelimitador() {
		new DominioTemplate().load();
		final List<UnidadeLayoutImportacao> unidades = Lists.newArrayList();
		Dominio identificador = Fixture.from(Dominio.class).gimme("tipoRegistro");
		Dominio carteira = Fixture.from(Dominio.class).gimme("carteira");
		Dominio quantidade = Fixture.from(Dominio.class).gimme("quantidade");
		Dominio valor = Fixture.from(Dominio.class).gimme("valorBruto");
		Dominio total = Fixture.from(Dominio.class).gimme("total");
		
		final UnidadeLayoutImportacao unidade1 = mockUnidade("1", Lists.newArrayList(new CampoLayoutImportacao(identificador, "1"), 
				new CampoLayoutImportacao(carteira, "AGORA")));
		final UnidadeLayoutImportacao unidade2 = mockUnidade("2", Lists.newArrayList(new CampoLayoutImportacao(identificador, "2"),
				new CampoLayoutImportacao(quantidade, "100000"),
				new CampoLayoutImportacao(valor, "50.00")));
		final UnidadeLayoutImportacao unidade3 = mockUnidade("2", Lists.newArrayList(new CampoLayoutImportacao(identificador, "2"),
				new CampoLayoutImportacao(quantidade, "200000"),
				new CampoLayoutImportacao(valor, "150.00")));
		final UnidadeLayoutImportacao unidade4 = mockUnidade("3", Lists.newArrayList(new CampoLayoutImportacao(identificador, "3"),
				new CampoLayoutImportacao(total, "2")));
		
		final UnidadeLayoutImportacao unidade5 = mockUnidade("1", Lists.newArrayList(new CampoLayoutImportacao(identificador, "1"), 
				new CampoLayoutImportacao(carteira, "XP INVEST")));
		final UnidadeLayoutImportacao unidade6 = mockUnidade("2", Lists.newArrayList(new CampoLayoutImportacao(identificador, "2"),
				new CampoLayoutImportacao(quantidade, "350000"),
				new CampoLayoutImportacao(valor, "10.00")));
		final UnidadeLayoutImportacao unidade7 = mockUnidade("3", Lists.newArrayList(new CampoLayoutImportacao(identificador, "3"),
				new CampoLayoutImportacao(total, "1")));
		
		
		unidades.add(unidade1);
		unidades.add(unidade2);
		unidades.add(unidade3);
		unidades.add(unidade4);
		unidades.add(unidade5);
		unidades.add(unidade6);
		unidades.add(unidade7);
		
		return unidades;
	}
	
	public static UnidadeLayoutImportacao mockUnidadeImportacaoValidarCorretoraData() {
		Sessao sessaoRptParams = Fixture.from(Sessao.class).gimme("sessaoValidarDataCorretora");
		CampoLayoutImportacao identificador = mockCampoLayout("1L", "tipoRegistro", "32", null);
		CampoLayoutImportacao corretora = mockCampoLayout("2L", "corretora", "AGG", null);
		CampoLayoutImportacao data = mockCampoLayout("3L", "data", "20171227", "yyyyMMdd");

		UnidadeLayoutImportacao unidade1 = mockUnidadeXml("1", 
				sessaoRptParams, 
				Lists.newArrayList(identificador, 
				corretora,
				data));
		return unidade1;
	}
	
	public static UnidadeLayoutImportacao mockUnidadeImportacaoComErroValidacaoCorretoraOuData() {
		Sessao sessaoRptParams = Fixture.from(Sessao.class).gimme("sessaoValidarDataCorretora");
		CampoLayoutImportacao identificador = mockCampoLayout("1L", "tipoRegistro", "32", null);
		CampoLayoutImportacao corretora = mockCampoLayout("2L", "corretora", "IPA", null);
		CampoLayoutImportacao data = mockCampoLayout("3L", "data", "20171225", "yyyyMMdd");
		
		UnidadeLayoutImportacao unidade1 = mockUnidadeXml("1", 
				sessaoRptParams, 
				Lists.newArrayList(identificador, 
						corretora,
						data));
		return unidade1;
	}
	
	public static UnidadeLayoutImportacao mockUnidadeTransformacao() {
		UnidadeLayoutImportacao unidade = new UnidadeLayoutImportacao();
		unidade.setId("129");
		unidade.setSessao(Fixture.from(Sessao.class).gimme("sessao32Transformacao"));
		unidade.setCampos(UnidadeLayoutImportacaoMock.getCamposUnidadeTransformacao(unidade));
		return unidade;
	}
	
	public static List<UnidadeLayoutImportacao> mockUnidadeImportacaoXml() {
		Sessao sessaoRptParams = Fixture.from(Sessao.class).gimme("sessaoCinfRptParams");
		Sessao sessaoLftParams = Fixture.from(Sessao.class).gimme("sessaoCinfLftParams");
		CampoLayoutImportacao campoCodigoSessaoRptParams = mockCampoLayout("1L", "codigo", "11111", null);
		CampoLayoutImportacao campoFlagSessaoRptParams = mockCampoLayout("2L", "flag", "true", null);
		CampoLayoutImportacao campoFinanceiroSessaoRptParams = mockCampoLayout("3L", "financeiro", "100000", "999999.99");
		CampoLayoutImportacao campoDataSessaoRptParams = mockCampoLayout("4L", "data", "20170510", "yyyyMMdd");
		CampoLayoutImportacao campoQuantidadeSessaoRptParams = mockCampoLayout("5L", "quantidade", "100", "999");
		CampoLayoutImportacao campoValorSessaoRptParams = mockCampoLayout("6L", "valorBruto", "10", "99");
		
		CampoLayoutImportacao campoCodigoSessaoLftParams = mockCampoLayout("7L", "codigo", "11111", null);
		CampoLayoutImportacao campoPuSessaoLftParams = mockCampoLayout("8L", "pu", "100", "999");
		
		UnidadeLayoutImportacao unidade1 = mockUnidadeXml("1", 
				sessaoRptParams, 
				Lists.newArrayList(campoCodigoSessaoRptParams, 
				campoFlagSessaoRptParams,
				campoFinanceiroSessaoRptParams,
				campoDataSessaoRptParams,
				campoQuantidadeSessaoRptParams,
				campoValorSessaoRptParams));
		UnidadeLayoutImportacao unidade2 = mockUnidadeXml("2", 
				sessaoLftParams, 
				Lists.newArrayList(campoCodigoSessaoLftParams, 
						campoPuSessaoLftParams));
		
		return Lists.newArrayList(unidade1, unidade2);
	}
	
	private static UnidadeLayoutImportacao mockUnidadeXml(final String id,
			final Sessao sessao,
			final List<CampoLayoutImportacao> campos) {
		Fixture
			.of(UnidadeLayoutImportacao.class)
			.addTemplate(sessao.getCodigo(), new Rule(){{
				this.add("id", id);
				this.add("sessao", sessao);
				this.add("campos", campos);
			}});
		return Fixture
					.from(UnidadeLayoutImportacao.class)
					.gimme(sessao.getCodigo());
	}
	
	private static CampoLayoutImportacao mockCampoLayout(
			final String idCodigo,
			final String dominio, 
			final String valor,
			final String pattern) {
		Fixture
			.of(CampoLayoutImportacao.class)
			.addTemplate(idCodigo, new Rule(){{
				this.add("id", idCodigo);
				this.add("campo", this.one(Dominio.class, dominio));
				this.add("valor", valor);
				this.add("pattern", pattern);
			}});
		return Fixture
					.from(CampoLayoutImportacao.class)
					.gimme(idCodigo);
	}

	private static UnidadeLayoutImportacao mockUnidade(final String idSessao, final List<CampoLayoutImportacao> campos) {
		final UnidadeLayoutImportacao unidadeImportacao = new UnidadeLayoutImportacao();
		final Sessao sessao = new Sessao();
		sessao.setId(idSessao);
		sessao.setCodigo(idSessao);
		sessao.setNome(idSessao);
		
		unidadeImportacao.setId(idSessao);
		unidadeImportacao.setCampos(campos);
		unidadeImportacao.setSessao(sessao);
		for (final CampoLayoutImportacao c : campos) {
			c.setUnidade(unidadeImportacao);
		}
		return unidadeImportacao;
	}
	
	private static List<CampoLayoutImportacao> getCamposUnidadeTransformacao(final UnidadeLayoutImportacao unidade) {
		Fixture
		.of(CampoLayoutImportacao.class).addTemplate("tipoRegistroTransformacao", new Rule(){{
			this.add("id", "1");
			this.add("campo", this.one(Dominio.class, "tipoRegistro"));
			this.add("valor", "32");
			this.add("unidade", unidade);
		}});
		
		Fixture
		.of(CampoLayoutImportacao.class).addTemplate("tipoMercadoTransformacao", new Rule(){{
			this.add("id", "2");
			this.add("campo", this.one(Dominio.class, "tipoMercado"));
			this.add("valor", "VST");
			this.add("unidade", unidade);
		}});
		
		Fixture
		.of(CampoLayoutImportacao.class).addTemplate("corretoraTransformacao", new Rule(){{
			this.add("id", "3");
			this.add("campo", this.one(Dominio.class, "corretora"));
			this.add("valor", "AGORA");
			this.add("unidade", unidade);
		}});
		
		return Lists.newArrayList(Fixture.from(CampoLayoutImportacao.class).gimme("tipoRegistroTransformacao"),
				Fixture.from(CampoLayoutImportacao.class).gimme("tipoMercadoTransformacao"),
				Fixture.from(CampoLayoutImportacao.class).gimme("corretoraTransformacao"));
		
	}
	
}
