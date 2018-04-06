package br.com.totvs.cia.parametrizacao.mock;

import br.com.totvs.cia.cadastro.agente.model.Agente;

public class AgenteMock {

	public static Agente mockAgora() {
		Agente agente = new Agente("1", "1", "AGORA", null, "1346");
		return agente;
	}

	
}
