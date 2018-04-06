package br.com.totvs.cia.gateway.core.equivalencia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.json.RemetenteJson;
import br.com.totvs.cia.gateway.amplis.equivalencia.service.RemetenteAmplisService;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class RemetenteGatewayService {

	@Autowired
	private RemetenteAmplisService remetenteAmplisService;

	public List<RemetenteJson> getAll(final SistemaEnum sistema){

		try {
			
			if(sistema.isAmplis()){
				return this.remetenteAmplisService.getAll();
			}
		
			throw new RuntimeException("Sistema não identificado");
			
		} catch (Exception e) {
			
			throw new GatewayException("Remetentes não encontradas.");
			
		}
	}	
}