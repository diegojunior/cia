package br.com.totvs.cia.gateway.amplis.equivalencia.converter;

import org.springframework.stereotype.Component;

import br.com.totvs.amplis.api.client.json.TipoEquivalenciaJson;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;

@Component
public class TipoEquivalenciaAmplisConverter extends JsonGatewayConverter<TipoEquivalenciaJson, br.com.totvs.cia.cadastro.equivalencia.json.TipoEquivalenciaJson>{

	@Override
	public br.com.totvs.cia.cadastro.equivalencia.json.TipoEquivalenciaJson convertFrom(final TipoEquivalenciaJson model) {
		return new br.com.totvs.cia.cadastro.equivalencia.json.TipoEquivalenciaJson(model.getId(), model.getCodigo());
	}
}
