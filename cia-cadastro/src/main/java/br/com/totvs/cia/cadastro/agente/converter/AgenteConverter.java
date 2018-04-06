package br.com.totvs.cia.cadastro.agente.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.agente.json.AgenteJson;
import br.com.totvs.cia.cadastro.agente.model.Agente;
import br.com.totvs.cia.cadastro.agente.service.AgenteService;
import br.com.totvs.cia.infra.converter.JsonConverter;

@Component
public class AgenteConverter extends JsonConverter<Agente, AgenteJson> {

	@Autowired
	private AgenteService agenteService;
	
	@Override
	public AgenteJson convertFrom(final Agente model) {
		return new AgenteJson(model);
	}

	@Override
	public Agente convertFrom(final AgenteJson json) {
		Agente agente = this.agenteService.findByCodigo(json.getCodigo());
		if (agente != null) {
			return new Agente(agente);
		}
		return new Agente(json);
	}
}
