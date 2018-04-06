package br.com.totvs.cia.infra.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "br.com.totvs.cia" })
public class CiaAppWebConfiguration extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/assets/**").addResourceLocations("/WEB-INF/assets/");
		registry.addResourceHandler("/source/**").addResourceLocations("/WEB-INF/source/");
		registry.addResourceHandler("/pages/**").addResourceLocations("/WEB-INF/pages/");
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	@Bean
	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2).groupName("cia")
				.apiInfo(this.apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("br.com.totvs.cia"))
				.paths(PathSelectors.any()).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Serviços do projeto CIA.")
				.description("Conciliador com Inteligência Automática")
				.version("1.0.0").build();
	}

	@Bean
	public InternalResourceViewResolver internalResourceViewResolver() {
		final InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/WEB-INF/pages/");
		resolver.setSuffix("");
		return resolver;

	}
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
	    final CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
	   // Arquivo limitado em 200 MegaBytes
	    commonsMultipartResolver.setMaxUploadSize(419430400);
	    commonsMultipartResolver.setMaxUploadSizePerFile(419430400);
	    
	    // set any fields
	    return commonsMultipartResolver; 
	}
	
	@Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
	
}