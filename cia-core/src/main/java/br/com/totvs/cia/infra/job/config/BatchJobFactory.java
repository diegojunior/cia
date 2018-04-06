package br.com.totvs.cia.infra.job.config;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

@Configuration
public class BatchJobFactory {
	
	@Autowired
	private BatchJobConfiguration batchJobConfiguration;

	public JobLauncher createAsyncJobLauncher() {
		
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
		jobLauncher.setJobRepository(this.batchJobConfiguration.createJobRepository());
		
		return jobLauncher;
	}
}