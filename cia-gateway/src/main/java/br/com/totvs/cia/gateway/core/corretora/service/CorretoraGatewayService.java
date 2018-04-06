package br.com.totvs.cia.gateway.core.corretora.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.cadastro.agente.json.AgenteJson;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.gateway.amplis.corretora.service.CorretoraAmplisService;

@Service
public class CorretoraGatewayService {
	
	@Autowired
	private CorretoraAmplisService corretoraAmplisService;

	public List<AgenteJson> getAll(final SistemaEnum sistema){

		if(sistema.isAmplis()){
			return this.corretoraAmplisService.getAll();
		}

		throw new RuntimeException("Sistema não identificado");
	}
}