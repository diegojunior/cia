package br.com.totvs.cia.infra.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.stereotype.Component;

@Component
public class CiaAutenticadorService {

	@Autowired
	private AmplisAuthenticationProvider amplisAuthenticationProvider; 
	
	public AuthenticationProvider provider() {
		return amplisAuthenticationProvider;
	}
}
