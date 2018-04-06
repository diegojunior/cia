package br.com.totvs.cia.infra.converter;

import java.util.List;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.infra.model.Model;

import com.google.common.collect.Lists;

public abstract class JsonConverter<M extends Model, J extends Json> {

	public abstract J convertFrom(M model);
	
	public abstract M convertFrom(J json);
	
	public List<J> convertListModelFrom(final List<M> models) {
		List<J> jsons = Lists.newArrayList();
		
		for (M model : models){
			jsons.add(convertFrom(model));
		}
		
		return jsons;
	}
	
	public List<M> convertListJsonFrom(final List<J> jsons) {
		List<M> models = Lists.newArrayList();
		
		for (J json : jsons){
			models.add(convertFrom(json));
		}
		
		return models;
	}
}
