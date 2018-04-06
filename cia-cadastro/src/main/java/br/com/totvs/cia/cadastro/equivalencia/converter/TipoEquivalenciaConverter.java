package br.com.totvs.cia.cadastro.equivalencia.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.equivalencia.json.TipoEquivalenciaJson;
import br.com.totvs.cia.cadastro.equivalencia.model.TipoEquivalencia;
import br.com.totvs.cia.cadastro.equivalencia.repository.TipoEquivalenciaRepository;
import br.com.totvs.cia.infra.converter.JsonConverter;

@Component
public class TipoEquivalenciaConverter extends JsonConverter<TipoEquivalencia, TipoEquivalenciaJson>{

	@Autowired
	private TipoEquivalenciaRepository tipoEquivalenciaRepository;

	@Override
	public TipoEquivalenciaJson convertFrom(final TipoEquivalencia model) {
		return new TipoEquivalenciaJson(model);
	}

	@Override
	public TipoEquivalencia convertFrom(final TipoEquivalenciaJson json) {
		try {
			if (json.getId() != null) {
				return this.tipoEquivalenciaRepository.getOne(json.getId());
			}

			return new TipoEquivalencia(json);
		} catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}
}