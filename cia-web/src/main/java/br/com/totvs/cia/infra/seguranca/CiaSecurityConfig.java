
package br.com.totvs.cia.infra.seguranca;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.stereotype.Component;

@Configuration
@EnableWebSecurity
public class CiaSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final Logger LOG = Logger.getLogger(CiaSecurityConfig.class);

	@Autowired
	private CiaAutenticadorService amplisAuthentication;

	@Autowired
	private AuthenticationFailureHandler loginFailureHandler;

	@Autowired
	public void configureGlobalSecurity(final AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.amplisAuthentication.provider());
	}

	@Override
	protected void configure(final HttpSecurity http) throws Exception {

		http
		.authorizeRequests()
		.antMatchers("/index", "/login/**", "/expired.html", "/invalidSession.html").permitAll()
		.anyRequest().authenticated()
		.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("username")
				.passwordParameter("password")
				.defaultSuccessUrl("/home", true)
				.failureHandler(this.loginFailureHandler)
			.and()
				.logout()
				.deleteCookies("JSESSIONID")
				.logoutUrl("/logout")
				.logoutSuccessUrl("/login?logout")
			.and().csrf().disable()
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
				.invalidSessionUrl("/invalidSession.html")
				.maximumSessions(10)
				.maxSessionsPreventsLogin(false)
				.expiredUrl("/expired.html")
				.sessionRegistry(this.sessionRegistry());

	}

	@Bean
	public SessionRegistry sessionRegistry() {
		final SessionRegistry sessionRegistry = new SessionRegistryImpl();
		return sessionRegistry;
	}

	@Override
	public void configure(final WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/assets/**");
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		return new HttpSessionEventPublisher();
	}

	@Component
	public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

		@Autowired
		public AuthenticationFailureHandler() {
			super();
		}

		@Override
		public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
				final AuthenticationException exception) throws IOException, ServletException {
			this.setDefaultFailureUrl(this.getFailureUrl(request));
			super.onAuthenticationFailure(request, response, exception);
		}

		private String getFailureUrl(final HttpServletRequest request) {
			final String refererUrl = request.getHeader("Referer");
			LOG.info("in AuthenticationFailureHandler, referrerUrl: " + refererUrl);
			if (refererUrl.indexOf("/invalidSession") != -1) {
				return "/login?invalid";
			}
			if (refererUrl.indexOf("/expired") != -1) {
				return "/login?expired";
			} else {
				return "/login?error";
			}
		}
	}

}
