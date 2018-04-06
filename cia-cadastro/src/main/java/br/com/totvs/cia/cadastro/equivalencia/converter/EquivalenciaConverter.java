package br.com.totvs.cia.cadastro.equivalencia.converter;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.equivalencia.json.EquivalenciaJson;
import br.com.totvs.cia.cadastro.equivalencia.model.Equivalencia;
import br.com.totvs.cia.cadastro.equivalencia.service.EquivalenciaService;
import br.com.totvs.cia.infra.converter.JsonConverter;

@Component
public class EquivalenciaConverter extends JsonConverter<Equivalencia, EquivalenciaJson>{

	@Autowired
	private EquivalenciaService service;

	@Override
	public EquivalenciaJson convertFrom(final Equivalencia model) {
		return new EquivalenciaJson(model);
	}

	@Override
	public Equivalencia convertFrom(final EquivalenciaJson json) {
		return new Equivalencia(json);
	}
	
	@Override
	public List<Equivalencia> convertListJsonFrom(final List<EquivalenciaJson> jsons) {
		final List<String> ids = jsons.stream().map(json -> json.getIdLegado()).collect(Collectors.toList());
		final List<Equivalencia> equivalencias = this.service.getByIdsLegado(ids);
		List<EquivalenciaJson> novasEquivalencias = jsons.stream().filter(json ->  {
			return !equivalencias
					.stream()
					.map(equivalencia -> equivalencia.getIdLegado())
					.collect(Collectors.toList())
					.contains(json.getIdLegado());
		}).collect(Collectors.toList());
		for (EquivalenciaJson equi : novasEquivalencias) {
			equivalencias.add(convertFrom(equi));
		}
		return equivalencias;
	}
}