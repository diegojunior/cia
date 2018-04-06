package br.com.totvs.cia.gateway.core.infra.converter;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.json.Json;

public abstract class JsonGatewayConverter<O extends Object, J extends Json> {
	
	public abstract J convertFrom(O object);

	public List<J> convertListFrom(final List<O> objects) {
		List<J> jsons = Lists.newArrayList();
		
		for (O object : objects){
			jsons.add(convertFrom(object));
		}
		
		return jsons;
	}
}
