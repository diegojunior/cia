package br.com.totvs.cia.infra.seguranca;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import br.com.totvs.cia.infra.config.CiaSecurityPropertiesConfig;
import br.com.totvs.cia.login.json.PerfilJson;
import br.com.totvs.cia.login.json.UsuarioJson;

@Component
public class AmplisAuthenticationProvider implements AuthenticationProvider {
	
	private static final Logger log = Logger.getLogger(AmplisAuthenticationProvider.class);

	private static final String AUTHENTICATION_URL = "authentication.url";

	@Autowired
	private CiaSecurityPropertiesConfig ciaPropertiesConfig;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		UsuarioJson usuarioJson = new UsuarioJson();
		usuarioJson.setUsername(authentication.getName());
		usuarioJson.setPassword(authentication.getCredentials().toString());
		
		try {
			HttpEntity<UsuarioJson> request = new HttpEntity<UsuarioJson>(usuarioJson);
			
			RestTemplate restTemplate = new RestTemplate();
			
			String url = this.ciaPropertiesConfig.getValue(AUTHENTICATION_URL);
			
			log.info("URL de autenticação: " + url);
			
			restTemplate.exchange(url, HttpMethod.POST, request, UsuarioJson.class);

		} catch (RuntimeException e) { //TODO [RENAN] verificar a forma correta de tratar as exceptions geradas pelo sistema legado de autenticação.
			
			log.error(e);
			throw new AuthenticationServiceException("Usuario ou senha invalido", e);
		
		} 
		
		PerfilJson perfil = new PerfilJson("ROLE_ADMIN");
		List<GrantedAuthority> perfis = new ArrayList<GrantedAuthority>();
		perfis.add(perfil);
		
		return new UsernamePasswordAuthenticationToken(usuarioJson.getUsername(), usuarioJson.getPassword(), perfis);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
	
}