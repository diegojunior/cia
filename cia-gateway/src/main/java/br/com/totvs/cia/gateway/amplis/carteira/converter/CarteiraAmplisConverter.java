package br.com.totvs.cia.gateway.amplis.carteira.converter;

import org.springframework.stereotype.Component;

import br.com.totvs.amplis.api.client.json.CarteiraJson;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;

@Component
public class CarteiraAmplisConverter extends JsonGatewayConverter<CarteiraJson, br.com.totvs.cia.cadastro.carteira.json.ClienteJson> {	
	
	@Override
	public br.com.totvs.cia.cadastro.carteira.json.ClienteJson convertFrom(final CarteiraJson carteiraAmplisJson) {
		String idAmplis = carteiraAmplisJson.getId();
		String codigo = carteiraAmplisJson.getCodigo();
		String nome = carteiraAmplisJson.getNome();
		
		return new br.com.totvs.cia.cadastro.carteira.json.ClienteJson(idAmplis, codigo, nome);
	}
	
}
