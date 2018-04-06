package br.com.totvs.cia.gateway.amplis.equivalencia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.http.TipoEquivalenciaResourceApi;
import br.com.totvs.amplis.api.client.json.TipoEquivalenciaJson;
import br.com.totvs.cia.gateway.amplis.equivalencia.converter.TipoEquivalenciaAmplisConverter;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;

@Service
public class TipoEquivalenciaAmplisService {
	
	@Autowired
	private TipoEquivalenciaAmplisConverter converter;
	
	@Autowired
	private AmplisWsClient client;

	public List<br.com.totvs.cia.cadastro.equivalencia.json.TipoEquivalenciaJson> getAll() {
		try {
			TipoEquivalenciaResourceApi tipoEquivalenciaResourceApi = this.client.getTipoEquivalenciaResourceApi();
			List<TipoEquivalenciaJson> tiposEquivalenciaJson = tipoEquivalenciaResourceApi.listAll();
			return this.converter.convertListFrom(tiposEquivalenciaJson);
		} catch (ApiException e) {
			e.printStackTrace();
			//TODO [RENAN] tratar exception
		}
		return null;
	}
}