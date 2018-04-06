package br.com.totvs.cia.infra.config;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import br.com.totvs.cia.infra.async.CiaAsync;
import br.com.totvs.cia.infra.config.persistence.CiaJpaConfiguration;
import br.com.totvs.cia.infra.seguranca.CiaSecurityConfig;
import br.com.totvs.cia.infra.web.CORSFilter;
import br.com.totvs.cia.infra.web.CiaAppWebConfiguration;

@Configuration
public class CiaInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		return new Class[] {CiaAppWebConfiguration.class, CiaJpaConfiguration.class, CiaSecurityConfig.class, CiaSecurityPropertiesConfig.class, CiaAsync.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}

	@Override
	protected String[] getServletMappings() {
		return new String[] {"/"};
	}
	
	@Override
	protected Filter[] getServletFilters() {
		final Filter[] singleton = { new CORSFilter(), new OpenEntityManagerInViewFilter() };
		return singleton;
	}
	
	@Override
	public void onStartup(final ServletContext servletContext) throws ServletException {
		super.onStartup(servletContext);
		servletContext.setInitParameter("spring.profiles.active", "dev");
	}
	
}
