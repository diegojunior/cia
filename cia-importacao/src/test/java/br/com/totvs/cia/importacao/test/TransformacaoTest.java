package br.com.totvs.cia.importacao.test;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.Lists;

import br.com.six2six.fixturefactory.Fixture;
import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.totvs.cia.cadastro.equivalencia.converter.EquivalenciaConverter;
import br.com.totvs.cia.cadastro.equivalencia.json.EquivalenciaJson;
import br.com.totvs.cia.gateway.core.equivalencia.service.EquivalenciaGatewayService;
import br.com.totvs.cia.importacao.converter.UnidadeImportacaoConverter;
import br.com.totvs.cia.importacao.job.TransformacaoExecutor;
import br.com.totvs.cia.importacao.json.CampoImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.mock.ImportacaoMock;
import br.com.totvs.cia.importacao.mock.UnidadeImportacaoProcessamentoJsonMock;
import br.com.totvs.cia.importacao.mock.UnidadeLayoutImportacaoMock;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.dominio.service.DominioService;
import br.com.totvs.cia.parametrizacao.mock.DominioMock;
import br.com.totvs.cia.parametrizacao.mock.TransformacaoMock;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoEquivalencia;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoContextService;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoEquivalenciaStrategy;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoFixoStrategy;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoService;
import br.com.totvs.cia.parametrizacao.validacao.service.ValidacaoArquivoService;
@RunWith(MockitoJUnitRunner.class)
public class TransformacaoTest {
	
	@Mock
	private ImportacaoService importacaoService;
	
	@Mock
	private UnidadeImportacaoConverter unidadeImportacaoConverter;
	
	@Mock
	private TransformacaoService transformacaoService;
	
	@Mock
	private TransformacaoContextService transformacaoContextService;
	
	@Mock
	private ValidacaoArquivoService validacaoService;
	
	@Mock
	private EquivalenciaGatewayService equivalenciaGatewayService;
	
	@Mock
	private DominioService dominioService;
	
	@Mock
	private EquivalenciaConverter equivalenciaConverter;
	
	@InjectMocks
	private TransformacaoExecutor transformacaoExecutor;
	
	private final TransformacaoFixoStrategy transformacaoFixo = new TransformacaoFixoStrategy();

	@BeforeClass
	public static void setUpTemplate() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.importacao.template.loader");
	}
	
	@Test
	public void testTransformacaoFixa() throws Exception {
		Importacao importacao = ImportacaoMock.mockPosicionalCinf();
		Transformacao transformacao = TransformacaoMock.mockTransformacaoFixaTipoMercadoCinf();
		UnidadeImportacaoProcessamentoJson unidade = UnidadeImportacaoProcessamentoJsonMock.mockUnidadesTransformacao();
		UnidadeLayoutImportacao unidadeLayoutMock = UnidadeLayoutImportacaoMock.mockUnidadeTransformacao();
		Dominio tipoMercado = Fixture.from(Dominio.class).gimme("tipoMercado");
		List<Transformacao> transformacoes = Lists.newArrayList(transformacao);
		List<Dominio> dominios = DominioMock.allMocks();
		Mockito
			.when(this.importacaoService.findOne("124"))
			.thenReturn(importacao);
		Mockito
			.when(this.transformacaoService.getBy(importacao.getLayout().getTipoLayout(), 
					importacao.getLayout().getCodigo()))
			.thenReturn(transformacoes);
		Mockito
			.when(this.transformacaoContextService.loadStrategies(importacao.getSistema(), transformacao))
			.thenReturn(this.transformacaoFixo);
		Mockito
			.when(this.dominioService.findAll())
			.thenReturn(dominios);
		Mockito
			.when(this.unidadeImportacaoConverter.convertFrom(unidade, dominios))
			.thenReturn(unidadeLayoutMock);
		
		this.transformacaoFixo.loadItens(importacao.getSistema(), transformacao);
		for (CampoImportacaoProcessamentoJson campo : unidade.getCampos()) {
			if (campo.getCampo().equals(tipoMercado.getCodigo())) {
				Assert.assertEquals(campo.getValor(), "VST");
			}
		}
		this.transformacaoExecutor.config(importacao.getSistema(), transformacoes);
		this.transformacaoExecutor.transformar(unidadeLayoutMock.getCampos(), transformacoes);
		unidadeLayoutMock.getCampos().forEach(campo -> {
			if(campo.getCampo().getCodigo().equals("tipoMercado"))
				Assert.assertEquals(campo.getValor(), "TTT");
		});
		
	}
	
	@Test
	public void testTransformacaoEquvalencia() throws Exception {
		TransformacaoEquivalenciaStrategy transformacaoEquivalenciaStrategy = new TransformacaoEquivalenciaStrategy(this.equivalenciaGatewayService, this.equivalenciaConverter);
		Importacao importacao = ImportacaoMock.mockPosicionalCinfComTransformacaoEquivalencia();
		Transformacao transformacao = TransformacaoMock.mockTransformacaoEquivalenciaTipoMercadoCinf();
		List<Transformacao> transformacoes = Lists.newArrayList(transformacao);
		final ItemTransformacaoEquivalencia itemTransformacao = (ItemTransformacaoEquivalencia) transformacao.getItem();
		List<EquivalenciaJson> equivalenciaJson = Lists.newArrayList(new EquivalenciaJson(importacao.getEquivalencias().iterator().next()));
		UnidadeImportacaoProcessamentoJson unidade = UnidadeImportacaoProcessamentoJsonMock.mockUnidadesTransformacao();
		UnidadeLayoutImportacao unidadeLayoutMock = UnidadeLayoutImportacaoMock.mockUnidadeTransformacao();
		Dominio corretora = Fixture.from(Dominio.class).gimme("corretora");
		Mockito
			.when(this.equivalenciaGatewayService.getEquivalenciasBy(importacao.getSistema(), 
					itemTransformacao.getRemetente().getCodigo(), 
					itemTransformacao.getTipoEquivalencia().getIdLegado()))
			.thenReturn(equivalenciaJson);
		Mockito
			.when(this.equivalenciaConverter.convertListJsonFrom(equivalenciaJson))
			.thenReturn(importacao.getEquivalencias());
		Mockito
			.when(this.importacaoService.findOne("129"))
			.thenReturn(importacao);
		Mockito
			.when(this.transformacaoService.getBy(importacao.getLayout().getTipoLayout(), 
				importacao.getLayout().getCodigo()))
			.thenReturn(transformacoes);
		Mockito
			.when(this.transformacaoContextService.loadStrategies(importacao.getSistema(), transformacao))
			.thenReturn(transformacaoEquivalenciaStrategy);
		
		transformacaoEquivalenciaStrategy.loadItens(importacao.getSistema(), transformacao);
		for (CampoImportacaoProcessamentoJson campo : unidade.getCampos()) {
			if (campo.getCampo().equals(corretora.getCodigo())) {
				Assert.assertEquals(campo.getValor(), "AGG");
			}
		}
		this.transformacaoExecutor.config(importacao.getSistema(), transformacoes);
		this.transformacaoExecutor.transformar(unidadeLayoutMock.getCampos(), transformacoes);
		unidadeLayoutMock.getCampos().forEach(campo -> {
			if(campo.getCampo().getCodigo().equals("correora"))
				Assert.assertEquals(campo.getValor(), "AGORA");
		});
	}
	
}
