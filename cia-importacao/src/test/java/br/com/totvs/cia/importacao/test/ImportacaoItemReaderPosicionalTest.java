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
import br.com.totvs.cia.importacao.exception.ImportacaoArquivoParseException;
import br.com.totvs.cia.importacao.job.ImportacaoItemLoteUnidadeReader;
import br.com.totvs.cia.importacao.job.ImportacaoItemReader;
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
public class ImportacaoItemReaderPosicionalTest {
	
	private static final String ID_IMPORTACAO = "124";
	
	@Autowired
	private ImportacaoItemReader importacaoItemReader;
	
	@Autowired
	private ImportacaoItemLoteUnidadeReader importacaoItemLoteReader;
	
	@BeforeClass
	public static void setupTemplates() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.importacao.template.loader");
	}
	
	@Test
	public void testImportacaoItemReaderPosicional() throws Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("importacao", ID_IMPORTACAO);
		List<UnidadeLayoutImportacao> mockUnidadesImportacao = UnidadeLayoutImportacaoMock.mockUnidadesImportacaoCinfPosicional();
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParametersBuilder.toJobParameters());
		Integer totalReader = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
			int totalUnidade = 0;
			ItemReader<UnidadeImportacaoProcessamentoJson> reader = this.importacaoItemReader.reader(ID_IMPORTACAO);
			UnidadeImportacaoProcessamentoJson unidade = null;
			while ((unidade = reader.read()) != null) {
				UnidadeLayoutImportacao unidadeLayoutImportacaoMock = mockUnidadesImportacao.get(totalUnidade);
				int index = 0;
				for (CampoImportacaoProcessamentoJson campo : unidade.getCampos()) {
					CampoLayoutImportacao campoLayoutImportacao = unidadeLayoutImportacaoMock.getCampos().get(index);
					Assert.assertEquals(campo.getValor(), campoLayoutImportacao.getValor());
					index++;
				}
				totalUnidade++;
			}
			
			this.importacaoItemReader.close();
			return totalUnidade;
		});
		Assert.assertTrue(totalReader == 4);
	}
	
	@Test
	public void testImportacaoItemLoteReaderPosicional() throws UnexpectedInputException, ParseException, Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("importacao", ID_IMPORTACAO);
		List<UnidadeLayoutImportacao> mockUnidadesImportacao = UnidadeLayoutImportacaoMock.mockUnidadesImportacaoCinfPosicional();
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParametersBuilder.toJobParameters());
		Integer totalReader = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
			int totalUnidade = 0;
			int chunkLote = 300;
			ItemReader<LoteUnidadeImportacao> reader = this.importacaoItemLoteReader.reader(ID_IMPORTACAO, chunkLote);
			LoteUnidadeImportacao lote = null;
			while ((lote = reader.read()) != null) {
				for (UnidadeLoteImportacao unidadeLote : lote.getUnidades()) {
					UnidadeLayoutImportacao unidadeLayoutImportacao = mockUnidadesImportacao.get(totalUnidade);
					for (CampoLayoutImportacao campo : unidadeLote.getCampos()) {
						CampoLayoutImportacao campoPorDominio = unidadeLayoutImportacao.getCampoPorDominio(campo.getCampo());
						Assert.assertEquals(campo.getValor(), campoPorDominio.getValor());
					}
					totalUnidade++;
				}
			}
			
			this.importacaoItemReader.close();
			return totalUnidade;
		});
		Assert.assertTrue(totalReader == 4);
	}
	
	@Test(expected = ImportacaoArquivoParseException.class)
	public void testImportacaoPosicionalTamanhoArquivoInvalido() throws Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		String importacaoIdParameter = "126";
		jobParametersBuilder.addString("importacao", importacaoIdParameter);
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParametersBuilder.toJobParameters());
		StepScopeTestUtils.doInStepScope(stepExecution, () -> {
			ItemReader<UnidadeImportacaoProcessamentoJson> reader = this.importacaoItemReader.reader(importacaoIdParameter);
			UnidadeImportacaoProcessamentoJson unidade = null;
			while ((unidade = reader.read()) != null) {
				Assert.assertEquals(4, unidade.getCampos().size());
			}
			
			return 0;
		});
	}
}
