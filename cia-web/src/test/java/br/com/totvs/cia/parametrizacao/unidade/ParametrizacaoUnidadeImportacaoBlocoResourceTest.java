package br.com.totvs.cia.parametrizacao.unidade;

import static br.com.totvs.cia.infra.exception.StatusCodeEnum.BUSINESS_EXCEPTION;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Lists;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.totvs.cia.config.AbstractTestConfig;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.dominio.repository.DominioRepository;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.repository.LayoutDelimitadorRepository;
import br.com.totvs.cia.parametrizacao.unidade.importacao.converter.ParametrizacaoUnidadeImportacaoBlocoConverter;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ParametrizacaoUnidadeImportacaoBlocoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.LinhaBlocoParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.ParametrizacaoUnidadeImportacaoBlocoRepository;

@Transactional
public class ParametrizacaoUnidadeImportacaoBlocoResourceTest extends AbstractTestConfig {

	private static final String URL_BASE = "/api/v1/parametrizacao/unidadeimportacao/bloco";

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private ParametrizacaoUnidadeImportacaoBlocoRepository repository;

	@Autowired
	private ParametrizacaoUnidadeImportacaoBlocoConverter converter;

	@Autowired
	private DominioRepository dominioRepository;

	@Autowired
	private LayoutDelimitadorRepository layoutDelimitadorRepository;

	@PersistenceContext
	private EntityManager entityManager;

	private ParametrizacaoUnidadeImportacaoBloco parametrizacao;

	@BeforeClass
	public static void setuTemplates() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.parametrizacao.template.loader");
	}
	
	@Before
	public void build() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		this.parametrizacao = this.setupParametrizacao();
	}

	@Test
	public void testSearchByCodigo() throws Exception {
		this.saveParametrizacao();

		this.mockMvc
				.perform(get(URL_BASE + "/search")
						.param("codigo", "Param 1"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].codigo", Matchers.containsString("Param 1")));
	}
	
	@Test
	public void testSearchByCodigoLayout() throws Exception {
		this.saveParametrizacao();
		
		this.mockMvc
			.perform(get(URL_BASE + "/search")
					.param("codigoLayout", this.parametrizacao.getLayout().getCodigo()))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
			.andExpect(MockMvcResultMatchers.jsonPath("$[0].codigo", Matchers.containsString("Param 1")));
	}
	
	@Test
	public void testRemoverParametrizacao() throws Exception {
		this.saveParametrizacao();
		final ParametrizacaoUnidadeImportacaoBlocoJson parametrizacaoJson = this.converter.convertFrom(this.parametrizacao);
		final List<ParametrizacaoUnidadeImportacaoBlocoJson> json = Lists.newArrayList();
		json.add(parametrizacaoJson);
		this.mockMvc
			.perform(MockMvcRequestBuilders.delete(URL_BASE + "/delete")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(this.asJsonString(json)))
			.andExpect(status().isOk());
	}

	@Test
	public void testInsertUnidade() throws Exception {
		final ParametrizacaoUnidadeImportacaoBlocoJson parametrizacaoJson = this.converter
				.convertFrom(this.parametrizacao);
		this.callPost(parametrizacaoJson);
	}

	@Test
	public void testInsertUnidadeCodigoDuplicado() throws Exception {
		this.saveParametrizacao();
		final ParametrizacaoUnidadeImportacaoBlocoJson parametrizacaoJson = this.gerarCopia();
		parametrizacaoJson.setCodigo("Param 1");
		this.mockMvc
				.perform(post(URL_BASE + "/incluir").contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(this.asJsonString(parametrizacaoJson)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(equalTo(BUSINESS_EXCEPTION.name()))))
				.andExpect(jsonPath("$.errors[0]", is(equalTo("Chave(s) Duplicada(s)."))));
	}

	@Test
	public void testInsertUnidadeLayoutDuplicado() throws Exception {
		this.saveParametrizacao();
		final ParametrizacaoUnidadeImportacaoBlocoJson parametrizacaoJson = this.gerarCopia();

		this.mockMvc
				.perform(post(URL_BASE + "/incluir").contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(this.asJsonString(parametrizacaoJson)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.status", is(equalTo(BUSINESS_EXCEPTION.name()))))
				.andExpect(jsonPath("$.errors[0]", is(equalTo(
						"Chave(s) Duplicada(s)."))));
	}

	private ParametrizacaoUnidadeImportacaoBlocoJson gerarCopia() {
		final ParametrizacaoUnidadeImportacaoBloco parametrizacaoNova = new ParametrizacaoUnidadeImportacaoBloco();
		parametrizacaoNova.setCodigo("Param 2");
		parametrizacaoNova.setLayout(this.parametrizacao.getLayout());
		parametrizacaoNova.setTipoLayout(TipoLayoutEnum.DELIMITADOR);
		parametrizacaoNova.setAbertura(this.parametrizacao.getAbertura());
		parametrizacaoNova.setFechamento(this.parametrizacao.getFechamento());
		final LinhaBlocoParametrizacaoUnidadeImportacao linha = new LinhaBlocoParametrizacaoUnidadeImportacao(null, 
				parametrizacaoNova, parametrizacaoNova.getFechamento(), Lists.newArrayList());
		linha.getCampos().addAll(parametrizacaoNova.getFechamento().getCampos());
		parametrizacaoNova.getLinhas().add(linha);
		parametrizacaoNova.getCampos().addAll(this.parametrizacao.getCampos());
		
		final ParametrizacaoUnidadeImportacaoBlocoJson parametrizacaoJson = this.converter
				.convertFrom(parametrizacaoNova);
		return parametrizacaoJson;
	}

	private void saveParametrizacao() {
		this.repository.save(this.parametrizacao);
		this.limparSessaoJpa();
	}

	private void callPost(final ParametrizacaoUnidadeImportacaoBlocoJson parametrizacaoJson) throws Exception {
		this.mockMvc
				.perform(post(URL_BASE + "/incluir").contentType(MediaType.APPLICATION_JSON_UTF8)
						.content(this.asJsonString(parametrizacaoJson)))
				.andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$.codigo", Matchers.is("Param 1")));
	}

	private ParametrizacaoUnidadeImportacaoBloco setupParametrizacao() {
		this.repository.deleteAll();
		this.layoutDelimitadorRepository.deleteAll();
		this.dominioRepository.deleteAll();

		final Dominio tipoRegistro = Fixture.from(Dominio.class).gimme("tipoRegistro");
		final Dominio corretora = Fixture.from(Dominio.class).gimme("corretora");
		final Dominio data = Fixture.from(Dominio.class).gimme("data");
		this.dominioRepository.save(tipoRegistro);
		this.dominioRepository.save(corretora);
		this.dominioRepository.save(data);

		final Campo campoTipoResgistro = new Campo(null, 1, tipoRegistro, "tipo de registro", null, 2, 1, 2, "99");
		final Campo campoCorretora = new Campo(null, 2, corretora, null, null, null, null, null, null);

		final Campo campoTipoResgistro1 = new Campo(null, 1, tipoRegistro, "tipo de registro", null, 2, 1, 2, "99");
		final Campo campoCorretora1 = new Campo(null, 2, corretora, null, null, null, null, null, null);
		final Campo campoData1 = new Campo(null, 3, data, null, null, null, null, null, null);

		final Sessao sessao = new Sessao();
		sessao.setCodigo("31");
		sessao.setNome("sessao31");
		sessao.getCampos().add(campoTipoResgistro);
		sessao.getCampos().add(campoCorretora);

		final Sessao sessao2 = new Sessao();
		sessao2.setCodigo("32");
		sessao2.setNome("sessao32");
		sessao2.getCampos().add(campoTipoResgistro1);
		sessao2.getCampos().add(campoCorretora1);
		sessao2.getCampos().add(campoData1);

		final LayoutDelimitador layoutDelimitador = new LayoutDelimitador();
		layoutDelimitador.setCodigo("CINF-delimitador");
		layoutDelimitador.setTipoLayout(TipoLayoutEnum.DELIMITADOR);
		layoutDelimitador.addSessao(sessao);
		layoutDelimitador.addSessao(sessao2);
		this.layoutDelimitadorRepository.save(layoutDelimitador);

		final ParametrizacaoUnidadeImportacaoBloco parametrizacaoUnidade = new ParametrizacaoUnidadeImportacaoBloco();
		final LinhaBlocoParametrizacaoUnidadeImportacao linha = new LinhaBlocoParametrizacaoUnidadeImportacao(null, 
				parametrizacaoUnidade, sessao2, Lists.newArrayList());
		linha.getCampos().addAll(sessao2.getCampos());
		parametrizacaoUnidade.setCodigo("Param 1");
		parametrizacaoUnidade.setLayout(layoutDelimitador);
		parametrizacaoUnidade.setTipoLayout(TipoLayoutEnum.DELIMITADOR);
		parametrizacaoUnidade.setAbertura(sessao);
		parametrizacaoUnidade.setFechamento(sessao2);
		parametrizacaoUnidade.getLinhas().add(linha);

		this.limparSessaoJpa();
		return parametrizacaoUnidade;
	}

	private void limparSessaoJpa() {
		this.entityManager.flush();
	}

}
