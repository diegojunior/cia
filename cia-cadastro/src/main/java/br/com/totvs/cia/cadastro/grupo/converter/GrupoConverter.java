package br.com.totvs.cia.cadastro.grupo.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.grupo.json.GrupoJson;
import br.com.totvs.cia.cadastro.grupo.model.Grupo;
import br.com.totvs.cia.cadastro.grupo.repository.GrupoRepository;
import br.com.totvs.cia.infra.converter.JsonConverter;

@Component
public class GrupoConverter extends JsonConverter<Grupo, GrupoJson>{

	@Autowired
	private GrupoRepository grupoRepository;

	@Override
	public GrupoJson convertFrom(final Grupo model) {
		return new GrupoJson(model);
	}

	@Override
	public Grupo convertFrom(final GrupoJson json) {
		try{
			if (json.getId() != null) {
				return this.grupoRepository.getOne(json.getId());
			}

			return new Grupo(json);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}
}