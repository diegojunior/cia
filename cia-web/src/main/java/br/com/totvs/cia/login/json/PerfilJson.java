package br.com.totvs.cia.login.json;

import org.springframework.security.core.GrantedAuthority;

import br.com.totvs.cia.infra.json.Json;

public class PerfilJson implements GrantedAuthority, Json {

	private static final long serialVersionUID = 1L;
	
	private String nome;
	
	public PerfilJson(String nome) {
		this.nome = nome;
	}
	
	@Override
	public String getAuthority() {
		return nome;
	}

}
