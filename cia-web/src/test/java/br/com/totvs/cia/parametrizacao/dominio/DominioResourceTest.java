package br.com.totvs.cia.parametrizacao.dominio;

import static br.com.totvs.cia.infra.exception.StatusCodeEnum.BUSINESS_EXCEPTION;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.totvs.cia.config.AbstractTestConfig;
import br.com.totvs.cia.parametrizacao.dominio.json.DominioJson;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.dominio.repository.DominioRepository;

public class DominioResourceTest extends AbstractTestConfig {
	
	private static final String URL_BASE = "/api/v1/parametrizacao/dominio";
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private DominioRepository dominioRepository;
	
	@BeforeClass
	public static void setupTemplates() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.parametrizacao.template.loader");
	}
	
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		Dominio carteira = Fixture.from(Dominio.class).gimme("carteira");
		this.dominioRepository.save(carteira);
	}
	
	@Test
	public void testAddDominio() throws Exception {
		Dominio ativo = Fixture.from(Dominio.class).gimme("ativo");
		DominioJson ativoJson = new DominioJson(ativo);
		this.mockMvc
			.perform(post(URL_BASE + "/incluir")
					.contentType(APPLICATION_JSON_UTF8)
					.content(this.asJsonString(ativoJson)))
			.andExpect(status().isCreated())
			.andExpect(header().string("location", is(equalTo("http://localhost/api/v1/parametrizacao/dominio/incluir/Ativo"))));
	}
	
	@Test
	public void testAddDuplicatedDominio() throws Exception {
		Dominio ativo = Fixture.from(Dominio.class).gimme("ativo");
		DominioJson ativoJson = new DominioJson(ativo);
		this.mockMvc
			.perform(post(URL_BASE + "/incluir")
					.contentType(APPLICATION_JSON_UTF8)
					.content(this.asJsonString(ativoJson)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status", is(equalTo(BUSINESS_EXCEPTION.name()))))
			.andExpect(jsonPath("$.errors[0]", is(equalTo("Chave(s) Duplicada(s)."))));
	}
	
	@Test
    public void testSearchToReturnOneItem() throws Exception {
        this.mockMvc
        	.perform(get(URL_BASE + "/list")
        			.param("codigo", "Carteira")
        			.param("tipo", "ALFANUMERICO"))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        	.andExpect(jsonPath("$", Matchers.hasSize(2)))
        	.andExpect(jsonPath("$[0].codigo", containsString("Carteira")));
    }
	
}
