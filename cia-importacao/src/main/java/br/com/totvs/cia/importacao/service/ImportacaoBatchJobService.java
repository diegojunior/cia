package br.com.totvs.cia.importacao.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.infra.job.config.BatchJobFactory;

@Service
public class ImportacaoBatchJobService {
	
	private static final Logger LOG = Logger.getLogger(ImportacaoBatchJobService.class);

	@Autowired
	@Qualifier(value = "importacaoJob")
	private Job job;
	
	@Autowired
	private BatchJobFactory batchJobFactory;
	
	public void run(final Importacao importacao) {
		
		final Map<String, JobParameter> jobParameters = new HashMap<String, JobParameter>();
		jobParameters.put("importacao", new JobParameter(importacao.getId()));
		jobParameters.put("dataImportacao", new JobParameter(new Date()));
		jobParameters.put("layoutType", new JobParameter(importacao.getLayout().getTipoLayout().name()));
		final JobLauncher launcher = this.batchJobFactory.createAsyncJobLauncher();
		
		try {
			LOG.info("### - Iniciando o job de importação - ###");
			launcher.run(this.job, new JobParameters(jobParameters));
			
		} catch (final JobExecutionAlreadyRunningException e){
			throw new RuntimeException(e);
		} catch (final JobRestartException e){
			throw new RuntimeException(e);
		} catch (final JobInstanceAlreadyCompleteException e){
			throw new RuntimeException(e);
		} catch (final JobParametersInvalidException e){
			throw new RuntimeException(e);
		}
	}
	
}
