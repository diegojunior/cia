package br.com.totvs.cia.carga.infra.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories("br.com.totvs.cia")
@PropertySources({
	@PropertySource("classpath:cia-carga-batch-job.properties"), 
	@PropertySource(value = "file:${CIA_HOME}/cia-carga-batch-job.properties", ignoreResourceNotFound = true)})
@EnableTransactionManagement
public class CiaCargaPropertiesConfiguration {
	
	@Resource
    private Environment env;
}