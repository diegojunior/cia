package br.com.totvs.cia.gateway.amplis.corretora.converter;

import org.springframework.stereotype.Component;

import br.com.totvs.amplis.api.client.json.CorretoraJson;
import br.com.totvs.cia.cadastro.agente.json.AgenteJson;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;

@Component
public class CorretoraAmplisConverter extends JsonGatewayConverter<CorretoraJson, AgenteJson> {
	
	@Override
	public AgenteJson convertFrom(final CorretoraJson corretoraAmplis) {
		String idAmplis = corretoraAmplis.getId();
		String codigo = corretoraAmplis.getCodigo();
		String descricao = corretoraAmplis.getNome();
		String codigoClearing = "";
	
		return new AgenteJson(idAmplis, codigo, descricao, codigoClearing);
	}
}