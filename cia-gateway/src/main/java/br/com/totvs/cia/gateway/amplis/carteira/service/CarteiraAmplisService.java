package br.com.totvs.cia.gateway.amplis.carteira.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.http.CarteiraResourceApi;
import br.com.totvs.amplis.api.client.json.PaginaCarteiraJson;
import br.com.totvs.cia.cadastro.carteira.json.ClienteJson;
import br.com.totvs.cia.gateway.amplis.carteira.converter.CarteiraAmplisConverter;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;

@Service
public class CarteiraAmplisService {
	
	private static final Logger log = Logger.getLogger(CarteiraAmplisService.class);
	
	@Autowired
	private AmplisWsClient client;
	
	@Autowired
	private CarteiraAmplisConverter converter;

	public List<ClienteJson> getAll() {
		try {
			CarteiraResourceApi resourceApi = this.client.getCarteiraResourceApi();
			return this.converter.convertListFrom(resourceApi.getAll());
		} catch (ApiException e) {
			log.error(e);
		}
		return null;
	}

	public List<ClienteJson> search(final String codigo, final Integer pagina, final Integer tamanho) {
		try {
			CarteiraResourceApi resourceApi = this.client.getCarteiraResourceApi();
			PaginaCarteiraJson paginaCarteiraJson = resourceApi.getPageBy(codigo, pagina, tamanho);
			return this.converter.convertListFrom(paginaCarteiraJson.getConteudo());
		} catch (ApiException e) {
			log.error(e);
		}
		return null;
	}
}