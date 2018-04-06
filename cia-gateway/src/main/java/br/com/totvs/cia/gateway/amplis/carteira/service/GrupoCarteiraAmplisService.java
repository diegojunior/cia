package br.com.totvs.cia.gateway.amplis.carteira.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.http.GrupoCarteiraResourceApi;
import br.com.totvs.amplis.api.client.json.PaginaGrupoCarteiraJson;
import br.com.totvs.cia.cadastro.grupo.json.GrupoJson;
import br.com.totvs.cia.gateway.amplis.carteira.converter.GrupoCarteiraAmplisConverter;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;

@Service
public class GrupoCarteiraAmplisService {
	
	private static final Logger log = Logger.getLogger(GrupoCarteiraAmplisService.class);
	
	@Autowired
	private AmplisWsClient client;
	
	@Autowired
	private GrupoCarteiraAmplisConverter converter;

	public List<GrupoJson> search(final String codigo, final Integer pagina, final Integer tamanho) {
		try {
			GrupoCarteiraResourceApi resourceApi = this.client.getGrupoCarteiraResourceApi();
			PaginaGrupoCarteiraJson paginaJson = resourceApi.getPageBy(codigo, pagina, tamanho);
			return this.converter.convertListFrom(paginaJson.getConteudo());
		} catch (ApiException e) {
			log.error(e);
		}
		return null;
	}
}