package br.com.totvs.cia.cadastro.equivalencia.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.equivalencia.json.RemetenteJson;
import br.com.totvs.cia.cadastro.equivalencia.model.Remetente;
import br.com.totvs.cia.cadastro.equivalencia.repository.RemetenteRepository;
import br.com.totvs.cia.infra.converter.JsonConverter;

@Component
public class RemetenteConverter extends JsonConverter<Remetente, RemetenteJson>{

	@Autowired
	private RemetenteRepository clienteRepository;

	@Override
	public RemetenteJson convertFrom(final Remetente model) {
		return new RemetenteJson(model);
	}

	@Override
	public Remetente convertFrom(final RemetenteJson json) {
		try{
			if (json.getId() != null) {
				return this.clienteRepository.getOne(json.getId());
			}

			return new Remetente(json);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}
}