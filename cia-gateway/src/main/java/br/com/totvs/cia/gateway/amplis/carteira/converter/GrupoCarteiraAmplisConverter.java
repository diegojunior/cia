package br.com.totvs.cia.gateway.amplis.carteira.converter;

import org.springframework.stereotype.Component;

import br.com.totvs.amplis.api.client.json.GrupoCarteiraJson;
import br.com.totvs.cia.cadastro.grupo.json.GrupoJson;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;

@Component
public class GrupoCarteiraAmplisConverter extends JsonGatewayConverter<GrupoCarteiraJson, GrupoJson> {	
	
	@Override
	public GrupoJson convertFrom(final GrupoCarteiraJson grupoCarteiraAmplisJson) {
		String idAmplis = grupoCarteiraAmplisJson.getId();
		String codigo = grupoCarteiraAmplisJson.getCodigo();
		
		return new GrupoJson(idAmplis, codigo);
	}
}