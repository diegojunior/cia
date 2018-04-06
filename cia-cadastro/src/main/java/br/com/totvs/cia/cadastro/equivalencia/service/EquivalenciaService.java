package br.com.totvs.cia.cadastro.equivalencia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.equivalencia.model.Equivalencia;
import br.com.totvs.cia.cadastro.equivalencia.repository.EquivalenciaRepository;

@Service
public class EquivalenciaService {

	@Autowired
	private EquivalenciaRepository repository;

	public Equivalencia getOne(final String id) {
		return this.repository.getOne(id);
	}
	
	public List<Equivalencia> getByIdsLegado(final List<String> idLegado) {
		
		if (idLegado.isEmpty()) return Lists.newArrayList();
		
		return this.repository.getByIdsLegado(idLegado);
	}
	
	public Equivalencia getByIdLegado(final String idLegado) {
		return this.repository.getByIdLegado(idLegado);
	}

	public <S extends Equivalencia> S save(final S entity) {
		return this.repository.save(entity);
	}

	public <S extends Equivalencia> List<S> save(final Iterable<S> entities) {
		return this.repository.save(entities);
	}
	
	
}
