package br.com.totvs.cia.parametrizacao.transformacao;

import static br.com.totvs.cia.infra.exception.StatusCodeEnum.BUSINESS_EXCEPTION;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.repository.LayoutPosicionalRepository;
import br.com.totvs.cia.parametrizacao.transformacao.converter.TransformacaoConverter;
import br.com.totvs.cia.parametrizacao.transformacao.json.TransformacaoJson;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixo;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixoDePara;
import br.com.totvs.cia.parametrizacao.transformacao.model.TipoTransformacaoEnum;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoService;

public class TransformacaoResourceTest extends AbstractTestConfig {

	private static final String URL_BASE = "/api/v1/parametrizacao/transformacao"; 
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private DominioRepository dominioRepository;
	
	@Autowired
	private LayoutPosicionalRepository layoutPosicionalRepository;
	
	@Autowired
	private TransformacaoService transformacaoService;
	
	@Autowired
	private TransformacaoConverter converter;
	
	@BeforeClass
	public static void setuClass() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.parametrizacao.template.loader");
	}
	
	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@Test
	@Transactional
	public void testTransformacaoFixaChaveDuplicada() throws Exception {
		Transformacao transformacao = this.setupTransformacao();
		TransformacaoJson transformacaoJson = this.converter.convertFrom(transformacao);
		this.transformacaoService.incluir(transformacao);
		this.mockMvc
			.perform(post(URL_BASE + "/incluir")
					.contentType(MediaType.APPLICATION_JSON_UTF8)
					.content(this.asJsonString(transformacaoJson)))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_UTF8))
			.andExpect(jsonPath("$.status", is(equalTo(BUSINESS_EXCEPTION.name()))))
			.andExpect(jsonPath("$.errors[0]", is(equalTo("Chave(s) Duplicada(s)."))));
	}
	
	@Test
	@Transactional
	public void testTransformacaoFixaMesmoCampo() throws Exception {
		Transformacao transformacao = this.setupTransformacao();
		ItemTransformacaoFixoDePara item1 = new ItemTransformacaoFixoDePara();
		item1.setDe("C");
		item1.setPara("Compra");
		ItemTransformacaoFixo item = (ItemTransformacaoFixo) transformacao.getItem();
		item.getItensDePara().add(item1);
		TransformacaoJson transformacaoJson = this.converter.convertFrom(transformacao);
		this.mockMvc
		.perform(post(URL_BASE + "/incluir")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(this.asJsonString(transformacaoJson)))
		.andExpect(MockMvcResultMatchers.status().isBadRequest())
		.andExpect(MockMvcResultMatchers.content().contentType(APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$.status", is(equalTo(BUSINESS_EXCEPTION.name()))))
		.andExpect(jsonPath("$.errors[0]", is(equalTo("O sistema já possui um campo De [ C ]"))));
	}

	@Transactional
	private Transformacao setupTransformacao() {
		this.transformacaoService.deleteAll();
		this.layoutPosicionalRepository.deleteAll();
		this.dominioRepository.deleteAll();
		
		Dominio tipoRegistro = Fixture.from(Dominio.class).gimme("tipoRegistro");
		Dominio corretora = Fixture.from(Dominio.class).gimme("corretora");
		Dominio data = Fixture.from(Dominio.class).gimme("data");
		this.dominioRepository.save(tipoRegistro);
		this.dominioRepository.save(corretora);
		this.dominioRepository.save(data);
		
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
		this.layoutPosicionalRepository.save(layoutPosicional);
		
		List<ItemTransformacaoFixoDePara> itens = Lists.newArrayList();
		ItemTransformacaoFixoDePara item1 = new ItemTransformacaoFixoDePara();
		item1.setDe("C");
		item1.setPara("Compra");
		itens.add(item1);
		ItemTransformacaoFixo item = new ItemTransformacaoFixo(itens);
		
		Transformacao transformacao = new Transformacao(null, TipoLayoutEnum.POSICIONAL, 
				layoutPosicional, sessao, 
				campoCorretora, TipoTransformacaoEnum.FIXO, 
				item);
		
		return transformacao;
	}
}
