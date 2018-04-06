package br.com.totvs.cia.config;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.totvs.cia.infra.config.persistence.CiaJpaConfiguration;
import br.com.totvs.cia.infra.web.CiaAppWebConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CiaAppWebConfiguration.class, CiaJpaConfiguration.class})
@WebAppConfiguration
@ActiveProfiles("test")
public abstract class AbstractTestConfig {

	public String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}