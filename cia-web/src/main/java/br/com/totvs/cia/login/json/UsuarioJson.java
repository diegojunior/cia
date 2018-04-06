package br.com.totvs.cia.login.json;

import br.com.totvs.cia.infra.json.Json;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName(value = "usuario")
public class UsuarioJson implements Json{
	
	private static final long serialVersionUID = 1L;

	private String username;
	
	private String password;
	
	public UsuarioJson() {
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
