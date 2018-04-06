package br.com.totvs.cia.gateway.amplis.equivalencia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.http.DestinatarioRemetenteResourceApi;
import br.com.totvs.amplis.api.client.json.DestinatarioRemetenteJson;
import br.com.totvs.cia.cadastro.equivalencia.json.RemetenteJson;
import br.com.totvs.cia.gateway.amplis.equivalencia.converter.RemetenteAmplisConverter;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;

@Service
public class RemetenteAmplisService {
	
	@Autowired
	private RemetenteAmplisConverter converter;
	
	@Autowired
	private AmplisWsClient client;

	public List<RemetenteJson> getAll() {
		try {
			DestinatarioRemetenteResourceApi destinatarioRemetenteResourceApi = this.client.getDestinatarioRemetenteResourceApi();
			List<DestinatarioRemetenteJson> destinatariosRemetentesJson = destinatarioRemetenteResourceApi.listAll();
			return this.converter.convertListFrom(destinatariosRemetentesJson);
		} catch (ApiException e) {
			e.printStackTrace();
			//TODO [RENAN] tratar exception
		}
		return null;
	}
}