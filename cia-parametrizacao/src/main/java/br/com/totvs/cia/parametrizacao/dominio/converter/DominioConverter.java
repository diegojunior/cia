package br.com.totvs.cia.parametrizacao.dominio.converter;

import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.dominio.json.DominioJson;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;

@Component
public class DominioConverter extends JsonConverter<Dominio, DominioJson> {

	@Override
	public Dominio convertFrom(final DominioJson json) {

		return new Dominio(json);
	}

	@Override
	public DominioJson convertFrom(final Dominio model) {
		return new DominioJson(model);
	}
}