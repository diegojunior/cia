package br.com.totvs.cia.infra.converter;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.infra.model.Model;

public abstract class JsonObjectConverter<M extends Model, T extends Json> {

	public abstract Json convertFrom(M model);
	
	public abstract Model convertFrom(T json);
}
