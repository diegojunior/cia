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
import br.com.totvs.cia.parametrizacao.layout.converter.LayoutPosicionalConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutPosicionalJson;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutPosicionalService;

public class LayoutPosicionalResourceTest extends AbstractTestConfig {
	
	private static final String URL_BASE = "/api/v1/parametrizacao/layout/posicional";
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private DominioRepository dominioRepository;
	
	@Autowired
	private LayoutPosicionalService layoutService;
	
	@Autowired
	private LayoutPosicionalConverter layoutConverter;
	
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
		LayoutPosicional layout = this.instanciaUmLayoutComUmaSessaoDeTresCampos();
		LayoutPosicionalJson layoutJson = this.layoutConverter.convertFrom(layout);
		
		this.mockMvc
			.perform(post(URL_BASE + "/adicionar")
					.contentType(APPLICATION_JSON_UTF8)
					.content(this.asJsonString(layoutJson)))
			.andExpect(status().isOk());
	}
	
	@Test
	public void testChaveDuplicada() throws Exception {
		this.criaUmLayoutPosicionalComUmaSessaoDeTresCampos();
		
		LayoutPosicional layoutJaExistente = this.instanciaUmLayoutComUmaSessaoDeTresCampos();
		
		LayoutPosicionalJson layoutJson = this.layoutConverter.convertFrom(layoutJaExistente);

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
		this.criaUmLayoutPosicionalComUmaSessaoDeTresCampos();
		
        this.mockMvc
        	.perform(get(URL_BASE + "/list")
        			.param("codigo", "teste"))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        	.andExpect(jsonPath("$", Matchers.hasSize(1)))
        	.andExpect(jsonPath("$[0].codigo", containsString("Teste POSICIONAL")));
    }
	
	@Test
	@Transactional
    public void testAtivar() throws Exception {
		LayoutPosicional layoutInativo = this.criaUmLayoutPosicionalInativo();
		
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
		LayoutPosicional layoutAtivo = this.criaUmLayoutPosicionalAtivo();
		
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
		this.layoutService.deleteAll();
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
	private LayoutPosicional instanciaUmLayoutComUmaSessaoDeTresCampos() {
		List<Dominio> dominios = this.criaTresDominios();
		
		Campo campoTipoResgistro = new Campo(1, dominios.get(0), 2, 1, 2);
		Campo campoCorretora = new Campo(2, dominios.get(1), 6, 3, 8);
		Campo campoData = new Campo(3, dominios.get(2), 2, 9, 10);
		
		Sessao sessao = new Sessao();
		sessao.setCodigo("02");
		sessao.setNome("sessaoTeste");
		sessao.setTamanho(10);
		sessao.getCampos().add(campoTipoResgistro);
		sessao.getCampos().add(campoCorretora);
		sessao.getCampos().add(campoData);
		
		LayoutPosicional layout = new LayoutPosicional();
		layout.setCodigo("Teste POSICIONAL");
		layout.setTipoLayout(TipoLayoutEnum.POSICIONAL);
		layout.setStatus(StatusLayoutEnum.ATIVO);
		layout.getSessoes().add(sessao);
		return layout;
	}
	
	@Transactional
	private void criaUmLayoutPosicionalComUmaSessaoDeTresCampos() {
		LayoutPosicional layout = this.instanciaUmLayoutComUmaSessaoDeTresCampos();
		this.layoutService.save(layout);
	}
	
	@Transactional
	private LayoutPosicional criaUmLayoutPosicionalAtivo() {
		LayoutPosicional layout = this.instanciaUmLayoutComUmaSessaoDeTresCampos();
		layout.setStatus(StatusLayoutEnum.ATIVO);
		return this.layoutService.save(layout);
	}
	
	@Transactional
	private LayoutPosicional criaUmLayoutPosicionalInativo() {
		LayoutPosicional layout = this.instanciaUmLayoutComUmaSessaoDeTresCampos();
		layout.setStatus(StatusLayoutEnum.INATIVO);
		return this.layoutService.save(layout);
	}
}