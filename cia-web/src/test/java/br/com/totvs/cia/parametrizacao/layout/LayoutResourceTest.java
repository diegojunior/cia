package br.com.totvs.cia.parametrizacao.layout;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
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
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutXmlService;

@Ignore
public class LayoutResourceTest extends AbstractTestConfig {
	
	private static final String URL_BASE = "/api/v1/parametrizacao/layout/listBy";
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private DominioRepository dominioRepository;
	
	@Autowired
	private LayoutXmlService layoutService;
	
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
	@Transactional
    public void testListAll() throws Exception {
		this.criaUmLayoutXmlComUmaSessaoDeTresCampos();
		
        this.mockMvc
        	.perform(get(URL_BASE + "/XML"))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        	.andExpect(jsonPath("$", Matchers.hasSize(1)))
        	.andExpect(jsonPath("$[0].codigo", containsString("Teste XML")));
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
	private void criaUmLayoutXmlComUmaSessaoDeTresCampos() {
		LayoutXml layout = this.instanciaUmLayoutXMLComUmaSessaoDeTresCampos();
		this.layoutService.save(layout);
	}
	
	@Transactional
	private LayoutXml instanciaUmLayoutXMLComUmaSessaoDeTresCampos() {
		List<Dominio> dominios = this.criaTresDominios();
		
		Campo campoTipoResgistro = new Campo(1, dominios.get(0), "/teste/1", "99");
		Campo campoCorretora = new Campo(2, dominios.get(1), "/teste/2", "");
		Campo campoData = new Campo(3, dominios.get(2), "/teste/3", "DD/MM/YYYY");
		
		Sessao sessao = new Sessao();
		sessao.setCodigo("02");
		sessao.setNome("sessaoTeste");
		sessao.getCampos().add(campoTipoResgistro);
		sessao.getCampos().add(campoCorretora);
		sessao.getCampos().add(campoData);
		
		LayoutXml layout = new LayoutXml();
		layout.setCodigo("Teste XML");
		layout.setTagRaiz("/Document");
		layout.setTipoLayout(TipoLayoutEnum.XML);
		layout.setStatus(StatusLayoutEnum.ATIVO);
		layout.getSessoes().add(sessao);
		return layout;
	}
}