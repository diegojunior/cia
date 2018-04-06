package br.com.totvs.cia.gateway.amplis.corretora.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.http.CorretoraResourceApi;
import br.com.totvs.amplis.api.client.json.CorretoraJson;
import br.com.totvs.cia.cadastro.agente.json.AgenteJson;
import br.com.totvs.cia.gateway.amplis.corretora.converter.CorretoraAmplisConverter;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;

@Service
public class CorretoraAmplisService {
	
	@Autowired
	private CorretoraAmplisConverter converter;
	
	@Autowired
	private AmplisWsClient client;

	public List<AgenteJson> getAll() {
		try {
			CorretoraResourceApi corretoraResource = this.client.getCorretoraResourceApi();
			List<CorretoraJson> corretorasJson = corretoraResource.listaTodas();
			return this.converter.convertListFrom(corretorasJson);
		} catch (ApiException e) {
			e.printStackTrace();
			//TODO [RENAN] tratar exception
		}
		return null;
	}
}