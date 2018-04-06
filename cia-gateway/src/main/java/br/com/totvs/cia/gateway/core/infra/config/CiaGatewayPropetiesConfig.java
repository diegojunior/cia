package br.com.totvs.cia.gateway.core.infra.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

@Configuration
@PropertySources({
	@PropertySource("classpath:cia-gateway.properties"), 
	@PropertySource(value = "file:${CIA_HOME}/cia-gateway.properties", ignoreResourceNotFound = true)})
public class CiaGatewayPropetiesConfig {
	
	private static final String AMPLIS_WS__URL = "amplis.ws.url";
	
	@Resource
	private Environment env;
	
	public String getAmplisWsUrl() {
		return this.getValue(AMPLIS_WS__URL);
	}
	
	private String getValue(String key) {
		return env.getProperty(key);
	}
}