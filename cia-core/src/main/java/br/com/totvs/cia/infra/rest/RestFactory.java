package br.com.totvs.cia.infra.rest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestFactory {

	@Bean
    protected RestTemplate restTemplate() {
        return new RestTemplate();
    }
}


