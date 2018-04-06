package br.com.totvs.cia.carga.job;

import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.carga.service.CargaService;

@Component
public class CargaJobListener implements JobExecutionListener {
	
	@Autowired
	private CargaService cargaService;

	@Override
	public void beforeJob(JobExecution jobExecution) {
	}
	
	@Override
	public void afterJob(final JobExecution jobExecution) {
		this.concluir(jobExecution);
	}
	
	private void concluir(final JobExecution jobExecution) {
		final Boolean isHouveErro = this.isGerouException(jobExecution);
		if (isHouveErro) {
			this.cargaService.concluirComErro(jobExecution.getJobParameters().getString("carga"));
		} else {
			this.cargaService.concluir(jobExecution.getJobParameters().getString("carga"));
		}
	}
	
	private Boolean isGerouException(final JobExecution jobExecution) {
		if(jobExecution.getStatus().isUnsuccessful()){
			for (final Throwable error : jobExecution.getFailureExceptions()) {
				if (error instanceof RuntimeException) {
					return true;
				}
			}
		}
		return false;
	}
}