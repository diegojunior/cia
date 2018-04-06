package br.com.totvs.cia.gateway.core.equivalencia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.json.TipoEquivalenciaJson;
import br.com.totvs.cia.gateway.amplis.equivalencia.service.TipoEquivalenciaAmplisService;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class TipoEquivalenciaGatewayService {

	@Autowired
	private TipoEquivalenciaAmplisService tipoEquivalenciaAmplisService;

	public List<TipoEquivalenciaJson> getAll(final SistemaEnum sistema){

		try {
			
			if(sistema.isAmplis()){
				return this.tipoEquivalenciaAmplisService.getAll();
			}
		
			throw new RuntimeException("Sistema não identificado");
			
		} catch (Exception e) {
			
			throw new GatewayException("Tipos de Equivalencia não encontrados.");
			
		}
	}	
}