package br.com.totvs.cia.importacao.test;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.com.six2six.fixturefactory.loader.FixtureFactoryLoader;
import br.com.totvs.cia.importacao.job.ImportacaoBatchJob;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DependencyService.class, 
		DependencyRepository.class, 
		DependencyJob.class, 
		ImportacaoBatchJob.class, 
		JobLauncherTestUtils.class})
public class ImportacaoBatchTest {
	
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	
	@BeforeClass
	public static void setup() {
		FixtureFactoryLoader.loadTemplates("br.com.totvs.cia.importacao.template.loader");
	}
	
	@Test
	public void testJobImportacaoDelimitadorPontoVirgula() throws Exception {
		final String IMPORTACAO_ID = "123";
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("importacao", IMPORTACAO_ID)
				.addString("layoutType", "DELIMITADOR")
				.toJobParameters();
		JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);
		Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
		Assert.assertTrue(jobExecution.getAllFailureExceptions().isEmpty());
		Assert.assertTrue(jobExecution.getStepExecutions().size() == 2);
		this.verificaItensLidos(jobExecution, 9);
		this.verificaItensProcessados(jobExecution, 7);
		this.verificaItensBlocosLidos(jobExecution, 1);
		this.verificaItensBlocosProcessados(jobExecution, 1);
	}
	
	@Test
	public void testJobImportacaoPosicional() throws Exception {
		final String IMPORTACAO_ID = "124";
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("importacao", IMPORTACAO_ID)
				.addString("layoutType", "POSICIONAL")
				.toJobParameters();
		JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);
		Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
		Assert.assertTrue(jobExecution.getAllFailureExceptions().isEmpty());
		for (StepExecution step : jobExecution.getStepExecutions()) {
			if ("importacaoStep".equals(step.getStepName())) {
				this.assertImportacaoStep(step, 4, 4);
			}
			if ("loteUnidadeStep".equals(step.getStepName())) {
				this.assertLoteImportacaoStep(step, 1, 1);
			}
		}
		
	}
	
	@Test
	public void testJobImportacaoXml() throws Exception {
		final String IMPORTACAO_ID = "125";
		JobParameters jobParameters = new JobParametersBuilder()
				.addString("importacao", IMPORTACAO_ID)
				.addString("layoutType", "XML")
				.toJobParameters();
		JobExecution jobExecution = this.jobLauncherTestUtils.launchJob(jobParameters);
		Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
		Assert.assertTrue(jobExecution.getAllFailureExceptions().isEmpty());
		for (StepExecution step : jobExecution.getStepExecutions()) {
			if ("importacaoXmlStep".equals(step.getStepName())) {
				this.assertImportacaoStep(step, 2, 2);
			}
			if ("loteUnidadeStep".equals(step.getStepName())) {
				this.assertLoteImportacaoStep(step, 1, 1);
			}
		}
		
	}

	private void assertImportacaoStep(final StepExecution step, final int readCountExpected, final int writeCountExpected) throws Exception {
		Assert.assertEquals(ExitStatus.COMPLETED, step.getExitStatus());
		Assert.assertEquals(readCountExpected, step.getReadCount());
		Assert.assertEquals(writeCountExpected, step.getWriteCount());
	}
	
	private void assertLoteImportacaoStep(final StepExecution step, final int readCountExpected, final int writeCountExpected) {
		Assert.assertEquals(ExitStatus.COMPLETED, step.getExitStatus());
		Assert.assertEquals(readCountExpected, step.getReadCount());
		Assert.assertEquals(writeCountExpected, step.getWriteCount());
	}

	private void verificaItensLidos(final JobExecution jobExecution, final int qtdItensArquivo) {
		Iterator<StepExecution> iterator = jobExecution.getStepExecutions().iterator();
		while (iterator.hasNext()) {
			StepExecution step = iterator.next();
			if ("delimitadorStep".equals(step.getStepName())) {
				Assert.assertEquals(step.getReadCount(), qtdItensArquivo);		
			}
		}
	}

	private void verificaItensProcessados(final JobExecution jobExecution, final int qtdItensProcessados) {
		Iterator<StepExecution> iterator = jobExecution.getStepExecutions().iterator();
		while (iterator.hasNext()) {
			StepExecution step = iterator.next();
			if ("delimitadorStep".equals(step.getStepName())) {
				Assert.assertEquals(step.getWriteCount(), qtdItensProcessados);		
			}
		}
	}
	
	private void verificaItensBlocosLidos(final JobExecution jobExecution, final int qtdItensArquivo) {
		Iterator<StepExecution> iterator = jobExecution.getStepExecutions().iterator();
		while (iterator.hasNext()) {
			StepExecution step = iterator.next();
			if ("blocoUnidadeStep".equals(step.getStepName())) {
				Assert.assertEquals(step.getReadCount(), qtdItensArquivo);		
			}
		}
	}

	private void verificaItensBlocosProcessados(final JobExecution jobExecution, final int qtdItensProcessados) {
		Iterator<StepExecution> iterator = jobExecution.getStepExecutions().iterator();
		while (iterator.hasNext()) {
			StepExecution step = iterator.next();
			if ("blocoUnidadeStep".equals(step.getStepName())) {
				Assert.assertEquals(step.getWriteCount(), qtdItensProcessados);		
			}
		}
	}
}
