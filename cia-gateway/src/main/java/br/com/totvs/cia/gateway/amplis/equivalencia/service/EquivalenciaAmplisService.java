package br.com.totvs.cia.gateway.amplis.equivalencia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.http.EquivalenciaResourceApi;
import br.com.totvs.cia.cadastro.equivalencia.json.EquivalenciaJson;
import br.com.totvs.cia.gateway.amplis.equivalencia.converter.EquivalenciaGatewayConverter;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;

@Service
public class EquivalenciaAmplisService {

	@Autowired
	private AmplisWsClient client;
	
	@Autowired
	private EquivalenciaGatewayConverter converter;
	
	public List<EquivalenciaJson> getEquivalenciaBy(final String senderMnemonic, final String codigoTipoEquivalencia) throws ApiException {
		try {
			final EquivalenciaResourceApi equivalenciaResourceApi = this.client.getEquivalenciaResourceApi();
			final List<br.com.totvs.amplis.api.client.json.EquivalenciaJson> equivalencias = equivalenciaResourceApi.getBy(senderMnemonic, codigoTipoEquivalencia);
			return this.converter.convertListFrom(equivalencias);
		} catch (final ApiException e) {
			if (e.getCode() == HttpStatus.NOT_FOUND.value()) {
				return Lists.newArrayList();
			}
			throw e;
		}
	}
}