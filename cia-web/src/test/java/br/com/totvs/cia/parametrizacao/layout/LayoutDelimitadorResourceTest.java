package br.com.totvs.cia.parametrizacao.layout;

import static br.com.totvs.cia.infra.exception.StatusCodeEnum.BUSINESS_EXCEPTION;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.google.common.collect.Lists;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.totvs.cia.config.AbstractTestConfig;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.dominio.repository.DominioRepository;
import br.com.totvs.cia.parametrizacao.layout.converter.LayoutDelimitadorConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutDelimitadorJson;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoDelimitadorEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutDelimitadorService;

public class LayoutDelimitadorResourceTest extends AbstractTestConfig {
	
	private static final String URL_BASE = "/api/v1/parametrizacao/layout/delimitador";
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private DominioRepository dominioRepository;
	
	@Autowired
	private LayoutDelimitadorService layoutDelimitadorService;
	
	@Autowired
	private LayoutDelimitadorConverter layoutConverter;
	
	@BeforeClass
	public static void setupTemplates() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.parametrizacao.template.loader");
	}
	
	@Before
	public void before() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		this.apagaLayouts();
		this.apagaDominios();
	}
	
	@After
	public void after() {
		this.apagaLayouts();
		this.apagaDominios();
	}
	
	@Test
	public void testAdicionar() throws Exception {
		LayoutDelimitador layout = this.instanciaUmLayoutDelimitadorComCodigoDeSessaoETresCampos();
		LayoutDelimitadorJson layoutJson = this.layoutConverter.convertFrom(layout);
		
		this.mockMvc
			.perform(post(URL_BASE + "/adicionar")
					.contentType(APPLICATION_JSON_UTF8)
					.content(this.asJsonString(layoutJson)))
			.andExpect(status().isOk());
	}
	
	@Test
	public void testChaveDuplicada() throws Exception {
		this.criaUmLayoutDelimitadorComCodigoDeSessaoETresCampos();
		
		LayoutDelimitador layoutJaExistente = this.instanciaUmLayoutDelimitadorComCodigoDeSessaoETresCampos();
		
		LayoutDelimitadorJson layoutJson = this.layoutConverter.convertFrom(layoutJaExistente);

		this.mockMvc
			.perform(post(URL_BASE + "/adicionar")
					.contentType(APPLICATION_JSON_UTF8)
					.content(this.asJsonString(layoutJson)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status", is(equalTo(BUSINESS_EXCEPTION.name()))))
			.andExpect(jsonPath("$.errors[0]", is(equalTo("Chave(s) Duplicada(s)."))));
	}
	
	@Test
	@Transactional
    public void testSearch() throws Exception {
		this.criaUmLayoutDelimitadorComCodigoDeSessaoETresCampos();
		
        this.mockMvc
        	.perform(get(URL_BASE + "/list")
        			.param("codigo", "teste"))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        	.andExpect(jsonPath("$", Matchers.hasSize(1)))
        	.andExpect(jsonPath("$[0].codigo", containsString("Teste DELIMITADOR")));
    }
	
	@Test
	@Transactional
    public void testAtivar() throws Exception {
		LayoutDelimitador layoutInativo = this.criaUmLayoutDelimitadorInativo();
		
        this.mockMvc
        	.perform(put(URL_BASE + "/ativar")
        			.contentType(APPLICATION_JSON_UTF8)
        			.content(layoutInativo.getId().getBytes()))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        	.andExpect(jsonPath("$.status.codigo", is(StatusLayoutEnum.ATIVO.getCodigo())));
    }
	
	@Test
	@Transactional
    public void testInativar() throws Exception {
		LayoutDelimitador layoutAtivo = this.criaUmLayoutDelimitadorAtivo();
		
        this.mockMvc
        	.perform(put(URL_BASE + "/inativar")
        			.contentType(APPLICATION_JSON_UTF8)
        			.content(layoutAtivo.getId().getBytes()))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        	.andExpect(jsonPath("$.status.codigo", is(StatusLayoutEnum.INATIVO.getCodigo())));
    }
	
	@Transactional
	private void apagaDominios() {
		this.dominioRepository.deleteAll();
	}
	
	@Transactional
	private void apagaLayouts() {
		this.layoutDelimitadorService.deleteAll();
	}
	
	@Transactional
	private List<Dominio> criaTresDominios() {
		List<Dominio> dominios = Lists.newArrayList();
		Dominio tipoRegistro = Fixture.from(Dominio.class).gimme("tipoRegistro");
		Dominio corretora = Fixture.from(Dominio.class).gimme("corretora");
		Dominio data = Fixture.from(Dominio.class).gimme("data");
		dominios.add(this.dominioRepository.save(tipoRegistro));
		dominios.add(this.dominioRepository.save(corretora));
		dominios.add(this.dominioRepository.save(data));
		return dominios;
	}
	
	@Transactional
	private LayoutDelimitador instanciaUmLayoutDelimitadorComCodigoDeSessaoETresCampos() {
		List<Dominio> dominios = this.criaTresDominios();
		
		Campo campoTipoResgistro = new Campo(1, dominios.get(0), "99");
		Campo campoCorretora = new Campo(2, dominios.get(1));
		Campo campoData = new Campo(3, dominios.get(2));
		
		Sessao sessao = new Sessao();
		sessao.setCodigo("02");
		sessao.setNome("sessaoTeste");
		sessao.getCampos().add(campoTipoResgistro);
		sessao.getCampos().add(campoCorretora);
		sessao.getCampos().add(campoData);
		
		LayoutDelimitador layoutDelimitador = new LayoutDelimitador();
		layoutDelimitador.setCodigo("Teste DELIMITADOR");
		layoutDelimitador.setTipoLayout(TipoLayoutEnum.DELIMITADOR);
		layoutDelimitador.setTipoDelimitador(TipoDelimitadorEnum.PONTO_VIRGULA);
		layoutDelimitador.setStatus(StatusLayoutEnum.ATIVO);
		layoutDelimitador.getSessoes().add(sessao);
		return layoutDelimitador;
	}
	
	@Transactional
	private void criaUmLayoutDelimitadorComCodigoDeSessaoETresCampos() {
		LayoutDelimitador layout = this.instanciaUmLayoutDelimitadorComCodigoDeSessaoETresCampos();
		this.layoutDelimitadorService.save(layout);
	}
	
	@Transactional
	private LayoutDelimitador criaUmLayoutDelimitadorAtivo() {
		LayoutDelimitador layout = this.instanciaUmLayoutDelimitadorComCodigoDeSessaoETresCampos();
		layout.setStatus(StatusLayoutEnum.ATIVO);
		return this.layoutDelimitadorService.save(layout);
	}
	
	@Transactional
	private LayoutDelimitador criaUmLayoutDelimitadorInativo() {
		LayoutDelimitador layout = this.instanciaUmLayoutDelimitadorComCodigoDeSessaoETresCampos();
		layout.setStatus(StatusLayoutEnum.INATIVO);
		return this.layoutDelimitadorService.save(layout);
	}
}