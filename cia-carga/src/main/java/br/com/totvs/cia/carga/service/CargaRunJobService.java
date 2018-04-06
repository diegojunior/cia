package br.com.totvs.cia.carga.service;

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

import br.com.totvs.cia.carga.json.TipoExecucaoCargaJsonEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.infra.exception.JobException;
import br.com.totvs.cia.infra.job.config.BatchJobFactory;

@Service
public class CargaRunJobService {
	
	private static final Logger log = Logger.getLogger(CargaRunJobService.class);

	@Autowired @Qualifier(value="cargaBatchJob")
	private Job job;

	@Autowired
	private BatchJobFactory batchJobFactory;

	public void run(final Carga carga, final TipoExecucaoCargaJsonEnum tipoExecucao) {
		try {
			Map<String, JobParameter> parameters = new HashMap<String, JobParameter>();

			parameters.put("carga", new JobParameter(carga.getId()));
			parameters.put("tipoExecucao", new JobParameter(tipoExecucao.getCodigo()));
			parameters.put("dataExecucao", new JobParameter(new Date().toString()));
			
			JobLauncher jobLauncher = this.batchJobFactory.createAsyncJobLauncher();
			jobLauncher.run(this.job, new JobParameters(parameters));

		} catch (JobExecutionAlreadyRunningException ex){
			log.error(ex);
			throw new JobException("Não foi possível executar o Job de Carga.", ex);
		} catch (JobRestartException ex){
			log.error(ex);
			throw new JobException("Não foi possível realizar o restart do Job de Carga.", ex);
		} catch (JobParametersInvalidException ex){
			log.error(ex);
			throw new JobException("Parametros do Job de Carga sao Invalidos.", ex);
		} catch (JobInstanceAlreadyCompleteException ex){
			log.error(ex);
			throw new JobException("Não foi possível executar o Job de Carga. Entre em contato com responsável pelo Sistema.", ex);
		}
		
	}
}