package br.com.totvs.cia.infra.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

@Configuration
@PropertySources({
	@PropertySource("classpath:cia-security.properties"), 
	@PropertySource(value = "file:${CIA_HOME}/cia-security.properties", ignoreResourceNotFound = true)})
public class CiaSecurityPropertiesConfig {

	@Resource
	private Environment env;
	
	public String getValue(String key) {
		return env.getProperty(key);
	}
	
}
