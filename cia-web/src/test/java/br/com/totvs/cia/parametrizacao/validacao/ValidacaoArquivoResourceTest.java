package br.com.totvs.cia.parametrizacao.validacao;

import static br.com.totvs.cia.infra.exception.StatusCodeEnum.BUSINESS_EXCEPTION;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.totvs.cia.config.AbstractTestConfig;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.dominio.repository.DominioRepository;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.repository.LayoutPosicionalRepository;
import br.com.totvs.cia.parametrizacao.validacao.converter.ValidacaoArquivoConverter;
import br.com.totvs.cia.parametrizacao.validacao.json.ValidacaoArquivoJson;
import br.com.totvs.cia.parametrizacao.validacao.model.CampoValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.LocalValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.TipoValidacaoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.ValidacaoArquivoInterno;
import br.com.totvs.cia.parametrizacao.validacao.service.ValidacaoArquivoService;

public class ValidacaoArquivoResourceTest extends AbstractTestConfig {

	private static final String URL_BASE = "/api/v1/parametrizacao/validacao/arquivo"; 
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ValidacaoArquivoConverter validacaoConverter;
	
	@Autowired
	private DominioRepository dominioRepository;
	
	@Autowired
	private LayoutPosicionalRepository layoutPosicionalRepository;
	
	@Autowired
	private ValidacaoArquivoService validacaoArquivoService;

	@BeforeClass
	public static void setuTemplates() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.parametrizacao.template.loader");
	}
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@Test
	@Transactional
	public void testListValidacoes() throws Exception {
		ValidacaoArquivoInterno validacaoInterna = setupValidacao();
		validacaoArquivoService.incluir(validacaoInterna);
		this.mockMvc
			.perform(get(URL_BASE + "/search")
				.param("tipolayout", "POSICIONAL")
				.param("layout", "CINF-posicional")
				.param("campoValidacao", "CORRETORA")
				.param("localValidacao", "INTERNO"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$", hasSize(1)));
	}
	
	@Test
	@Transactional
	public void testIncluirValidacoes() throws Exception {
		ValidacaoArquivoInterno validacaoInterna = setupValidacao();
		ValidacaoArquivoJson validacaoJson = this.validacaoConverter.convertFrom(validacaoInterna);
		this.mockMvc
			.perform(post(URL_BASE + "/incluir")
				.contentType(APPLICATION_JSON_UTF8)
				.content(this.asJsonString(validacaoJson)))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.campoValidacao.nome", Matchers.is("Corretora")));
	}
	
	@Test
	@Transactional
	public void testIncluirValidacaoComCodigoDuplicado() throws Exception {
		ValidacaoArquivoInterno validacaoInterna = setupValidacao();
		validacaoArquivoService.incluir(validacaoInterna);
		ValidacaoArquivoJson validacaoJson = this.validacaoConverter.convertFrom(validacaoInterna);
		this.mockMvc
		.perform(post(URL_BASE + "/incluir")
				.contentType(APPLICATION_JSON_UTF8)
				.content(this.asJsonString(validacaoJson)))
		.andExpect(status().isBadRequest())
		.andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.status", is(equalTo(BUSINESS_EXCEPTION.name()))))
		.andExpect(jsonPath("$.errors[0]", is(equalTo("Chave(s) Duplicada(s)."))));
	}
	
	@Transactional
	private ValidacaoArquivoInterno setupValidacao() {
		validacaoArquivoService.deleteAll();
		layoutPosicionalRepository.deleteAll();
		dominioRepository.deleteAll();
		
		Dominio tipoRegistro = Fixture.from(Dominio.class).gimme("tipoRegistro");
		Dominio corretora = Fixture.from(Dominio.class).gimme("corretora");
		Dominio data = Fixture.from(Dominio.class).gimme("data");
		dominioRepository.save(tipoRegistro);
		dominioRepository.save(corretora);
		dominioRepository.save(data);
		
		Campo campoTipoResgistro = new Campo(null, 1, tipoRegistro, "tipo de registro", null, 2, 1, 2, "99");
		Campo campoCorretora = new Campo(null, 2, corretora, null, null, null, null, null, null);
		Campo campoData = new Campo(null, 3, data, null, null, null, null, null, null);
		
		Sessao sessao = new Sessao();
		sessao.setCodigo("32");
		sessao.setNome("sessaoValidarDataCorretora");
		sessao.getCampos().add(campoTipoResgistro);
		sessao.getCampos().add(campoCorretora);
		sessao.getCampos().add(campoData);
		
		LayoutPosicional layoutPosicional = new LayoutPosicional();
		layoutPosicional.setCodigo("CINF-posicional");
		layoutPosicional.setTipoLayout(TipoLayoutEnum.POSICIONAL);
		layoutPosicional.getSessoes().add(sessao);
		layoutPosicionalRepository.save(layoutPosicional);
		
		ValidacaoArquivoInterno validacao = new ValidacaoArquivoInterno();
		validacao.setTipoValidacao(TipoValidacaoEnum.ARQUIVO);
		validacao.setTipoLayout(TipoLayoutEnum.POSICIONAL);
		validacao.setLayout(layoutPosicional);
		validacao.setLocalValidacao(LocalValidacaoArquivoEnum.INTERNO);
		validacao.setCampoValidacao(CampoValidacaoArquivoEnum.CORRETORA);
		validacao.setSessaoLayout(sessao);
		validacao.setCampoLayout(campoCorretora);
		
		return validacao;
	}
	
	
}
