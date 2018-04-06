package br.com.totvs.cia.parametrizacao.perfil;

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
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.TipoServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.service.ConfiguracaoServicoService;
import br.com.totvs.cia.config.AbstractTestConfig;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.dominio.repository.DominioRepository;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutPosicionalService;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.converter.PerfilConciliacaoConverter;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.PerfilConciliacaoJson;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.CampoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.ConfiguracaoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.StatusPerfilEnum;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.service.PerfilConciliacaoService;

public class PerfilConciliacaoResourceTest extends AbstractTestConfig {
	
	private static final String URL_BASE = "/api/v1/parametrizacao/perfilconciliacao";
	
	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ConfiguracaoServicoService configuracaoServicoService;
	
	@Autowired
	private DominioRepository dominioRepository;
	
	@Autowired
	private LayoutPosicionalService layoutService;
	
	@Autowired
	private PerfilConciliacaoService perfilService;
	
	@Autowired
	private PerfilConciliacaoConverter perfilConverter;
	
	@BeforeClass
	public static void setupTemplates() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.parametrizacao.template.loader");
	}
	
	@Before
	public void before() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context).build();
		this.apagaPerfis();
		this.apagaLayouts();
		this.apagaDominios();
		this.apagaConfiguracaoServico();
	}
	
	@After
	public void after() {
		this.apagaPerfis();
		this.apagaLayouts();
		this.apagaDominios();
		this.apagaConfiguracaoServico();
	}
	
	@Test
	@Transactional
    public void testSearch() throws Exception {
		this.criaUmPerfilDeSessaoDeLayoutComUmaChaveUmConciliavelEUmInformativo();
		
        this.mockMvc
        	.perform(get(URL_BASE + "/search")
        			.param("codigo", "teste"))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        	.andExpect(jsonPath("$", Matchers.hasSize(1)))
        	.andExpect(jsonPath("$[0].identificacao.codigo", containsString("PERFIL TESTE")));
    }
	
	@Test
	@Transactional
	public void testAdicionar() throws Exception {
		PerfilConciliacao perfil = this.instanciaUmPerfilDeSessaoDeLayoutComUmaChaveUmConciliavelEUmInformativo();
		PerfilConciliacaoJson perfilJson = this.perfilConverter.convertFrom(perfil);
		
		this.mockMvc
			.perform(post(URL_BASE + "/incluir")
					.contentType(APPLICATION_JSON_UTF8)
					.content(this.asJsonString(perfilJson)))
			.andExpect(status().isOk());
	}
	
	@Test
	@Transactional
	public void testChaveDuplicada() throws Exception {
		PerfilConciliacao perfil = this.criaUmPerfilDeSessaoDeLayoutComUmaChaveUmConciliavelEUmInformativo();
		
		PerfilConciliacaoJson perfilJson = this.perfilConverter.convertFrom(perfil);

		this.mockMvc
			.perform(post(URL_BASE + "/incluir")
					.contentType(APPLICATION_JSON_UTF8)
					.content(this.asJsonString(perfilJson)))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status", is(equalTo(BUSINESS_EXCEPTION.name()))))
			.andExpect(jsonPath("$.errors[0]", is(equalTo("Chave(s) Duplicada(s)."))));
	}
	
	@Test
	@Transactional
    public void testAtivar() throws Exception {
		PerfilConciliacao perfilInativo = this.criaUmPerfilInativo();
		
        this.mockMvc
        	.perform(put(URL_BASE + "/ativar")
        			.contentType(APPLICATION_JSON_UTF8)
        			.content(perfilInativo.getId().getBytes()))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        	.andExpect(jsonPath("$.identificacao.status.codigo", is(StatusPerfilEnum.ATIVO.getCodigo())));
    }
	
	@Test
	@Transactional
    public void testInativar() throws Exception {
		PerfilConciliacao perfilAtivo = this.criaUmPerfilAtivo();
		
        this.mockMvc
        	.perform(put(URL_BASE + "/inativar")
        			.contentType(APPLICATION_JSON_UTF8)
        			.content(perfilAtivo.getId().getBytes()))
        	.andExpect(status().isOk())
        	.andExpect(content().contentType(APPLICATION_JSON_UTF8))
        	.andExpect(jsonPath("$.identificacao.status.codigo", is(StatusPerfilEnum.INATIVO.getCodigo())));
    }
	
	@Transactional
	private void apagaConfiguracaoServico() {
		this.configuracaoServicoService.deleteAll();
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
	private void apagaPerfis() {
		this.perfilService.deleteAll();
	}
	
	@Transactional
	private PerfilConciliacao criaUmPerfilDeSessaoDeLayoutComUmaChaveUmConciliavelEUmInformativo() {
		PerfilConciliacao perfil = this.instanciaUmPerfilDeSessaoDeLayoutComUmaChaveUmConciliavelEUmInformativo();
		return this.perfilService.save(perfil);
	}
	
	@Transactional
	private PerfilConciliacao criaUmPerfilAtivo() {
		PerfilConciliacao perfil = this.instanciaUmPerfilDeSessaoDeLayoutComUmaChaveUmConciliavelEUmInformativo();
		perfil.setStatus(StatusPerfilEnum.ATIVO);
		return this.perfilService.save(perfil);
	}
	
	@Transactional
	private PerfilConciliacao criaUmPerfilInativo() {
		PerfilConciliacao perfil = this.instanciaUmPerfilDeSessaoDeLayoutComUmaChaveUmConciliavelEUmInformativo();
		perfil.setStatus(StatusPerfilEnum.INATIVO);
		return this.perfilService.save(perfil);
	}
	
	@Transactional
	private PerfilConciliacao instanciaUmPerfilDeSessaoDeLayoutComUmaChaveUmConciliavelEUmInformativo() {
		ConfiguracaoServico configuracaoServico = this.criaConfiguracaoServicoRendaVariavelAVista ();
		LayoutPosicional layout = this.criaUmLayoutPosicionalComTresDominios();
		CampoPerfilConciliacao campoChave = new CampoPerfilConciliacao();
		campoChave.setCampoCarga(configuracaoServico.getCampos().get(0));
		campoChave.setCampoImportacao(layout.getSessoes().get(0).getCampos().get(0).getDominio());
		campoChave.setIsChave(true);
		campoChave.setIsConciliavel(false);
		campoChave.setIsInformativo(false);
		campoChave.setOrdem(0);
		
		CampoPerfilConciliacao campoConciliavel = new CampoPerfilConciliacao();
		campoConciliavel.setCampoCarga(configuracaoServico.getCampos().get(1));
		campoConciliavel.setCampoImportacao(layout.getSessoes().get(0).getCampos().get(1).getDominio());
		campoConciliavel.setIsChave(false);
		campoConciliavel.setIsConciliavel(true);
		campoConciliavel.setIsInformativo(false);
		campoConciliavel.setOrdem(1);
		
		CampoPerfilConciliacao campoInformativo = new CampoPerfilConciliacao();
		campoInformativo.setCampoCarga(configuracaoServico.getCampos().get(2));
		campoInformativo.setIsChave(false);
		campoInformativo.setIsConciliavel(false);
		campoInformativo.setIsInformativo(true);
		campoInformativo.setOrdem(2);
		
		ConfiguracaoPerfilConciliacao configuracao = new ConfiguracaoPerfilConciliacao();
		configuracao.setServico(configuracaoServico);
		configuracao.setSessao(layout.getSessoes().get(0));
		configuracao.setConfiguracoes(Lists.newArrayList(campoChave, campoConciliavel, campoInformativo));
		
		PerfilConciliacao perfil = new PerfilConciliacao();
		perfil.setCodigo("PERFIL TESTE");
		perfil.setLayout(layout);
		perfil.setSistema(SistemaEnum.AMPLIS);
		perfil.setConfiguracao(configuracao);
		perfil.setStatus(StatusPerfilEnum.ATIVO);

		return perfil;
	}
	
	@Transactional
	private ConfiguracaoServico criaConfiguracaoServicoRendaVariavelAVista() {
		List<CampoConfiguracaoServico> campos = Lists.newArrayList();
		campos.add(new CampoConfiguracaoServico("tipoRegistro", "Tipo de Registro"));
		campos.add(new CampoConfiguracaoServico("corretora", "Corretora"));
		campos.add(new CampoConfiguracaoServico("data", "Data"));
		
		ConfiguracaoServico configuracaoServico = new ConfiguracaoServico();
		configuracaoServico.setCodigo("RVA");
		configuracaoServico.setServico(ServicoEnum.RENDAVARIAVEL_AVISTA);
		configuracaoServico.setSistema(SistemaEnum.AMPLIS);
		configuracaoServico.setTipoServico(TipoServicoEnum.POSICAO);
		configuracaoServico.setCampos(campos);
		
		return this.configuracaoServicoService.incluir(configuracaoServico);
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
	private LayoutPosicional criaUmLayoutPosicionalComTresDominios() {
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
		return this.layoutService.save(layout);
	}
}