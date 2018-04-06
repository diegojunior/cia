package br.com.totvs.cia.importacao.test;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.totvs.cia.importacao.converter.UnidadeImportacaoConverter;
import br.com.totvs.cia.importacao.exception.ValidacaoInternaException;
import br.com.totvs.cia.importacao.job.ImportacaoItemProcessor;
import br.com.totvs.cia.importacao.job.TransformacaoExecutor;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.mock.ImportacaoMock;
import br.com.totvs.cia.importacao.mock.UnidadeImportacaoProcessamentoJsonMock;
import br.com.totvs.cia.importacao.mock.UnidadeLayoutImportacaoMock;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.parametrizacao.dominio.service.DominioService;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.mock.DominioMock;
import br.com.totvs.cia.parametrizacao.mock.LayoutPosicionalMock;
import br.com.totvs.cia.parametrizacao.mock.ValidacaoInternaMock;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoService;
import br.com.totvs.cia.parametrizacao.validacao.model.LocalValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.TipoValidacaoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.ValidacaoArquivoInterno;
import br.com.totvs.cia.parametrizacao.validacao.service.ValidacaoArquivoService;


public class ValidacaoArquivoInternoTest {

	@Mock
	private UnidadeImportacaoConverter unidadeImportacaoConverter;
	
	@Mock
	private ImportacaoService importacaoService;

	@Mock
	private DominioService dominioService;
	
	@Mock
	private ValidacaoArquivoService validacaoService;
	
	@Mock
	private TransformacaoExecutor transformacaoExecutor;
	
	@Mock
	private TransformacaoService transformacaoService;
	
	@InjectMocks
	private final ImportacaoItemProcessor importacaoItemProcessor = new ImportacaoItemProcessor();
	
	private UnidadeImportacaoProcessamentoJson unidadeImportacaoCinfPosicional;
	
	
	@BeforeClass
	public static void setUpLoader() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.importacao.template.loader");
	}
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.unidadeImportacaoCinfPosicional = UnidadeImportacaoProcessamentoJsonMock.mockUnidadesCinfPosicional();
		Mockito
			.when(this.dominioService.findAll())
			.thenReturn(DominioMock.allMocks());
		
		Mockito
			.when(this.importacaoService.loadImportacaoAndUnidade("128"))
			.thenReturn(ImportacaoMock.mockPosicionalCinfComValidacaoCorretora());
		
	}
	
	@Test
	public void testProcessComValidacaoCorretora() throws Exception {
		UnidadeLayoutImportacao unidadeImportacaoCinf = UnidadeLayoutImportacaoMock.mockUnidadeImportacaoValidarCorretoraData();
		ValidacaoArquivoInterno validacaoCorretoraInterna = ValidacaoInternaMock.mockValidacaoInternaCorretora();
		LayoutPosicional layoutCinfPosicional = LayoutPosicionalMock.mockLayoutCinfPosicional();
		Mockito
			.when(this.unidadeImportacaoConverter.convertFrom(this.unidadeImportacaoCinfPosicional, DominioMock.allMocks()))
			.thenReturn(unidadeImportacaoCinf);
		Mockito
		.when(this.validacaoService.search(layoutCinfPosicional.getTipoLayout(), 
				layoutCinfPosicional.getCodigo(), 
				TipoValidacaoEnum.ARQUIVO, 
				null, 
				LocalValidacaoArquivoEnum.INTERNO))
		.thenReturn(Lists.newArrayList(validacaoCorretoraInterna));
		this.importacaoItemProcessor.carregaDominios();
		this.importacaoItemProcessor.carregaValidacoesTransformacoes("128");
		this.importacaoItemProcessor.process(this.unidadeImportacaoCinfPosicional);
	}
	
	@Test
	public void testProcessComValidacaoData() throws Exception {
		UnidadeLayoutImportacao unidadeImportacaoCinf = UnidadeLayoutImportacaoMock.mockUnidadeImportacaoValidarCorretoraData();
		ValidacaoArquivoInterno validacaoCorretoraInterna = ValidacaoInternaMock.mockValidacaoInternaData();
		LayoutPosicional layoutCinfPosicional = LayoutPosicionalMock.mockLayoutCinfPosicional();
		Mockito
			.when(this.unidadeImportacaoConverter.convertFrom(this.unidadeImportacaoCinfPosicional, DominioMock.allMocks()))
			.thenReturn(unidadeImportacaoCinf);
		Mockito
			.when(this.validacaoService.search(layoutCinfPosicional.getTipoLayout(), 
				layoutCinfPosicional.getCodigo(), 
				TipoValidacaoEnum.ARQUIVO, 
				null, 
				LocalValidacaoArquivoEnum.INTERNO))
			.thenReturn(Lists.newArrayList(validacaoCorretoraInterna));
		this.importacaoItemProcessor.carregaDominios();
		this.importacaoItemProcessor.carregaValidacoesTransformacoes("128");
		this.importacaoItemProcessor.process(this.unidadeImportacaoCinfPosicional);
	}
	
	@Test(expected = ValidacaoInternaException.class)
	public void testProcessComErroValidacaoData() throws Exception {
		UnidadeLayoutImportacao unidadeImportacaoCinf = UnidadeLayoutImportacaoMock.mockUnidadeImportacaoComErroValidacaoCorretoraOuData();
		ValidacaoArquivoInterno validacaoCorretoraInterna = ValidacaoInternaMock.mockValidacaoInternaData();
		LayoutPosicional layoutCinfPosicional = LayoutPosicionalMock.mockLayoutCinfPosicional();
		Mockito
		.when(this.unidadeImportacaoConverter.convertFrom(this.unidadeImportacaoCinfPosicional, DominioMock.allMocks()))
		.thenReturn(unidadeImportacaoCinf);
		Mockito
		.when(this.validacaoService.search(layoutCinfPosicional.getTipoLayout(), 
				layoutCinfPosicional.getCodigo(), 
				TipoValidacaoEnum.ARQUIVO, 
				null, 
				LocalValidacaoArquivoEnum.INTERNO))
		.thenReturn(Lists.newArrayList(validacaoCorretoraInterna));
		this.importacaoItemProcessor.carregaDominios();
		this.importacaoItemProcessor.carregaValidacoesTransformacoes("128");
		this.importacaoItemProcessor.process(this.unidadeImportacaoCinfPosicional);
	}
	
	@Test(expected = ValidacaoInternaException.class)
	public void testProcessComErroValidacaoCorretora() throws Exception {
		UnidadeLayoutImportacao unidadeImportacaoCinf = UnidadeLayoutImportacaoMock.mockUnidadeImportacaoComErroValidacaoCorretoraOuData();
		ValidacaoArquivoInterno validacaoCorretoraInterna = ValidacaoInternaMock.mockValidacaoInternaCorretora();
		LayoutPosicional layoutCinfPosicional = LayoutPosicionalMock.mockLayoutCinfPosicional();
		Mockito
			.when(this.unidadeImportacaoConverter.convertFrom(this.unidadeImportacaoCinfPosicional, DominioMock.allMocks()))
			.thenReturn(unidadeImportacaoCinf);
		Mockito
		.when(this.validacaoService.search(layoutCinfPosicional.getTipoLayout(), 
				layoutCinfPosicional.getCodigo(), 
				TipoValidacaoEnum.ARQUIVO, 
				null, 
				LocalValidacaoArquivoEnum.INTERNO))
		.thenReturn(Lists.newArrayList(validacaoCorretoraInterna));
		this.importacaoItemProcessor.carregaDominios();
		this.importacaoItemProcessor.carregaValidacoesTransformacoes("128");
		this.importacaoItemProcessor.process(this.unidadeImportacaoCinfPosicional);
	}
	
}
