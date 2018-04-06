package br.com.totvs.cia.gateway.amplis.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.totvs.amplis.api.client.ApiClient;
import br.com.totvs.cia.gateway.core.infra.config.CiaGatewayPropetiesConfig;

@Configuration
public class ApiClientFactory {
	
	@Autowired
	private CiaGatewayPropetiesConfig properties;
	
	@Bean
	protected ApiClient createApiClient() {
		ApiClient client = new ApiClient();
		client.setBasePath(this.properties.getAmplisWsUrl());
		return client;
	}

}
