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
import br.com.totvs.cia.importacao.job.ImportacaoItemBlocoReader;
import br.com.totvs.cia.importacao.job.ImportacaoItemDelimitadorReader;
import br.com.totvs.cia.importacao.json.CampoImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.mock.UnidadeLayoutImportacaoMock;
import br.com.totvs.cia.importacao.model.BlocoDelimitador;
import br.com.totvs.cia.importacao.model.CampoLayoutImportacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
@ContextConfiguration(classes = {DependencyService.class, 
		DependencyRepository.class, 
		DependencyJob.class})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, 
	StepScopeTestExecutionListener.class})
@RunWith(SpringJUnit4ClassRunner.class)
public class ImportacaoItemReaderDelimitadorTest {
	
	private static final String ID_IMPORTACAO = "123";
	
	@Autowired
	private ImportacaoItemDelimitadorReader importacaoItemDelimitadorReader;
	
	@Autowired
	private ImportacaoItemBlocoReader importacaoItemBlocoReader;
	
	@BeforeClass
	public static void setupTemplates() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.importacao.template.loader");
	}
	
	@Test
	public void testImportacaoItemReaderDelimitador() throws Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("importacao", ID_IMPORTACAO);
		List<UnidadeLayoutImportacao> mockUnidadesImportacao = UnidadeLayoutImportacaoMock.mockUnidadesImportacaoDelimitador();
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParametersBuilder.toJobParameters());
		Integer totalReader = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
			int totalUnidade = 0;
			ImportacaoItemDelimitadorReader reader = this.importacaoItemDelimitadorReader.loadReader(ID_IMPORTACAO);
			UnidadeImportacaoProcessamentoJson unidade = null;
			while ((unidade = reader.read()) != null) {
				if (!unidade.getCampos().isEmpty()) {
					UnidadeLayoutImportacao unidadeLayoutImportacaoMock = mockUnidadesImportacao.get(totalUnidade);
					int index = 0;
					for (CampoImportacaoProcessamentoJson campo : unidade.getCampos()) {
						CampoLayoutImportacao campoLayoutImportacao = unidadeLayoutImportacaoMock.getCampos().get(index);
						Assert.assertEquals(campo.getValor(), campoLayoutImportacao.getValor());
						index++;
					}
					totalUnidade++;
					
				}
			}
			
			this.importacaoItemDelimitadorReader.close();
			return totalUnidade;
		});
		Assert.assertTrue(totalReader == 7);
	}
	
	@Test
	public void testImportacaoItemBlocoReaderDelimitador() throws UnexpectedInputException, ParseException, Exception {
		JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
		jobParametersBuilder.addString("importacao", ID_IMPORTACAO);
		List<UnidadeLayoutImportacao> mockUnidadesImportacao = UnidadeLayoutImportacaoMock.mockUnidadesImportacaoDelimitador();
		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(jobParametersBuilder.toJobParameters());
		Integer totalReader = StepScopeTestUtils.doInStepScope(stepExecution, () -> {
			int totalUnidade = 0;
			ItemReader<BlocoDelimitador> reader = this.importacaoItemBlocoReader.reader(ID_IMPORTACAO);
			BlocoDelimitador bloco = null;
			while ((bloco = reader.read()) != null) {
				for (UnidadeLayoutImportacao unidade : bloco.getUnidadesLayoutImportacao()) {
					UnidadeLayoutImportacao unidadeLayoutImportacao = mockUnidadesImportacao.get(totalUnidade);
					for (CampoLayoutImportacao campo : unidade.getCampos()) {
						CampoLayoutImportacao campoPorDominio = unidadeLayoutImportacao.getCampoPorDominio(campo.getCampo());
						Assert.assertEquals(campo.getValor(), campoPorDominio.getValor());
					}
					totalUnidade++;
				}
			}
			
			return totalUnidade;
		});
		Assert.assertTrue(totalReader == 7);
	}
}
