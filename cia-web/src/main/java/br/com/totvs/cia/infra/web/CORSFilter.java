package br.com.totvs.cia.infra.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;

public class CORSFilter implements Filter {
	
	private static final String UTF8 = "UTF-8";  
	private static final String CONTENT_TYPE = "text/html; charset=UTF-8";

	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");

		response.setContentType(CONTENT_TYPE);
	    response.setCharacterEncoding(UTF8);

		HttpServletRequest request = (HttpServletRequest) req;
		
		// No CORS, o browser envia uma chamada options(preflight) para confirmar se uma chamada post está habilitada.
		final boolean isOptionsRequest = request.getMethod().equals(HttpMethod.OPTIONS);
		if (isOptionsRequest) {
			return;
		}
		
		chain.doFilter(req, res);
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {}
}