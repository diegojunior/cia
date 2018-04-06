package br.com.totvs.cia.importacao.test;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestExecutionListener;
import org.springframework.batch.test.StepScopeTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.totvs.cia.importacao.job.ImportacaoItemLoteUnidadeReader;
import br.com.totvs.cia.importacao.job.ImportacaoItemXmlReader;
import br.com.totvs.cia.importacao.json.CampoImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.mock.UnidadeLayoutImportacaoMock;
import br.com.totvs.cia.importacao.model.CampoLayoutImportacao;
import br.com.totvs.cia.importacao.model.LoteUnidadeImportacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.importacao.model.UnidadeLoteImportacao;
@ContextConfiguration(classes = {DependencyService.class, 
		DependencyRepository.class, 
		DependencyJob.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, 
	StepScopeTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportacaoItemReaderXmlTest {
	
	private static final String ID_IMPORTACAO = "125";
	
	@Autowired
	private ImportacaoItemXmlReader importacaoItemXmlReader;
	
	@Autowired
	private ImportacaoItemLoteUnidadeReader importacaoItemLoteReader;
	
	@BeforeClass
	public static void setupTemplates() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.importacao.template.loader");
	}
	
	@Test
	public void testImportacaoItemReaderXml() throws Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("importacao", ID_IMPORTACAO);
		List<UnidadeLayoutImportacao> mockUnidadesImportacao = UnidadeLayoutImportacaoMock.mockUnidadeImportacaoXml();
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParametersBuilder.toJobParameters());
		Integer totalReader = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
			int totalUnidade = 0;
			ItemReader<UnidadeImportacaoProcessamentoJson> reader = this.importacaoItemXmlReader.reader(ID_IMPORTACAO);
			UnidadeImportacaoProcessamentoJson unidade = null;
			while ((unidade = reader.read()) != null) {
				if (!unidade.getCampos().isEmpty()) {
					UnidadeLayoutImportacao unidadeLayoutImportacaoMock = mockUnidadesImportacao.get(totalUnidade);
					int index = 0;
					for (CampoImportacaoProcessamentoJson campoUnidade : unidade.getCampos()) {
						CampoLayoutImportacao campoLayoutImportacaoMock = unidadeLayoutImportacaoMock.getCampos().get(index);
						Assert.assertEquals(campoUnidade.getValor(), campoLayoutImportacaoMock.getValor());
						index++;
					}
					totalUnidade++;
					
				}
			}
			
			this.importacaoItemXmlReader.close();
			return totalUnidade;
		});
		Assert.assertTrue(totalReader == 2);
	}
	
	@Test
	public void testImportacaoItemLoteReaderXml() throws UnexpectedInputException, ParseException, Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("importacao", ID_IMPORTACAO);
		List<UnidadeLayoutImportacao> mockUnidadesImportacao = UnidadeLayoutImportacaoMock.mockUnidadeImportacaoXml();
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParametersBuilder.toJobParameters());
		Integer totalReader = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
			int totalUnidade = 0;
			ItemReader<LoteUnidadeImportacao> reader = this.importacaoItemLoteReader.reader(ID_IMPORTACAO, 300);
			LoteUnidadeImportacao lote = null;
			while ((lote = reader.read()) != null) {
				for (UnidadeLoteImportacao unidadeLote : lote.getUnidades()) {
					UnidadeLayoutImportacao unidadeLayoutImportacaoMock = mockUnidadesImportacao.get(totalUnidade);
					for (CampoLayoutImportacao campo : unidadeLote.getCampos()) {
						CampoLayoutImportacao campoPorDominioMock = unidadeLayoutImportacaoMock.getCampoPorDominio(campo.getCampo());
						Assert.assertEquals(campo.getValor(), campoPorDominioMock.getValor());
					}
					totalUnidade++;
				}
			}
			
			return totalUnidade;
		});
		Assert.assertTrue(totalReader == 2);
	}
	
	@Test
	public void testImportacaoItemLoteReaderXmlComParametrizacaoUnidade() throws UnexpectedInputException, ParseException, Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		String importacaoId = "127";
		jobParametersBuilder.addString("importacao", importacaoId);
		List<UnidadeLayoutImportacao> mockUnidadesImportacao = UnidadeLayoutImportacaoMock.mockUnidadeImportacaoXml();
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParametersBuilder.toJobParameters());
		Integer totalReader = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
			int totalUnidade = 0;
			ItemReader<LoteUnidadeImportacao> reader = this.importacaoItemLoteReader.reader(importacaoId, 300);
			LoteUnidadeImportacao lote = null;
			while ((lote = reader.read()) != null) {
				for (UnidadeLoteImportacao unidadeLote : lote.getUnidades()) {
					UnidadeLayoutImportacao unidadeLayoutImportacaoMock = mockUnidadesImportacao.get(totalUnidade);
					for (CampoLayoutImportacao campo : unidadeLote.getCampos()) {
						CampoLayoutImportacao campoPorDominioMock = unidadeLayoutImportacaoMock.getCampoPorDominio(campo.getCampo());
						Assert.assertEquals(campo.getValor(), campoPorDominioMock.getValor());
					}
					totalUnidade++;
				}
			}
			
			return totalUnidade;
		});
		Assert.assertTrue(totalReader == 2);
	}
}
