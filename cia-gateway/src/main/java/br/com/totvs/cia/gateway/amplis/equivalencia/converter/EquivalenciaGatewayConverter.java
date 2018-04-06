package br.com.totvs.cia.gateway.amplis.equivalencia.converter;

import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.equivalencia.json.EquivalenciaJson;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;

@Component
public class EquivalenciaGatewayConverter extends JsonGatewayConverter<br.com.totvs.amplis.api.client.json.EquivalenciaJson, EquivalenciaJson> {

	@Override
	public EquivalenciaJson convertFrom(final br.com.totvs.amplis.api.client.json.EquivalenciaJson object) {
		return new EquivalenciaJson(null, object.getId(), object.getCodigoInterno(), object.getCodigoExterno());
	}

}
