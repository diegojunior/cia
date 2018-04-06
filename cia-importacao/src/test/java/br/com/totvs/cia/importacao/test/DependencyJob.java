package br.com.totvs.cia.importacao.test;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import br.com.totvs.cia.importacao.converter.UnidadeImportacaoConverter;
import br.com.totvs.cia.importacao.converter.UnidadeLayoutImportacaoConverter;
import br.com.totvs.cia.infra.job.config.BatchJobConfiguration;
import br.com.totvs.cia.infra.job.config.BatchJobFactory;

@Configuration
@ComponentScan(basePackages = {"br.com.totvs.cia.importacao.service", "br.com.totvs.cia.importacao.job" })
public class DependencyJob {

	@Bean
	public BatchJobFactory getBatchJobFactory() {
		return Mockito.mock(BatchJobFactory.class);
	}

	@Bean
	public BatchJobConfiguration getBatchJobConfiguration() {
		return Mockito.mock(BatchJobConfiguration.class);
	}
	
	@Bean
	public UnidadeImportacaoConverter getUnidadeConverter() {
		return new UnidadeImportacaoConverter();
	}
	
	@Bean
	public UnidadeLayoutImportacaoConverter getUnidadeLayoutImportacaoConverter() {
		return new UnidadeLayoutImportacaoConverter();
	}
	
}
