package br.com.totvs.cia.carga.job;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import lombok.NoArgsConstructor;

@Configuration
@NoArgsConstructor
@PropertySources({
	@PropertySource("classpath:cia-carga-batch-job.properties"), 
	@PropertySource(value = "file:${CIA_HOME}/cia-carga-batch-job.properties", ignoreResourceNotFound = true)})
public class CargaJobConfig {
	
	@Resource
    private Environment env;
	
	private static final String QUANTIDADE_CLIENTES_JOB = "batch.quantidade.clientes.job";

	public Integer getQuantidadeClientesPorJob() {
		
		String quantidade = this.env.getProperty(QUANTIDADE_CLIENTES_JOB);
		
		if(quantidade != null) {
			return Integer.valueOf(quantidade);
		}
		
		return null;
		
	}
}