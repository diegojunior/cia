package br.com.totvs.cia.gateway.amplis.equivalencia.converter;

import org.springframework.stereotype.Component;

import br.com.totvs.amplis.api.client.json.DestinatarioRemetenteJson;
import br.com.totvs.cia.cadastro.equivalencia.json.RemetenteJson;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;

@Component
public class RemetenteAmplisConverter extends JsonGatewayConverter<DestinatarioRemetenteJson, RemetenteJson>{

	@Override
	public RemetenteJson convertFrom(final DestinatarioRemetenteJson model) {
		return new RemetenteJson(model.getId(), model.getCodigo());
	}
}
