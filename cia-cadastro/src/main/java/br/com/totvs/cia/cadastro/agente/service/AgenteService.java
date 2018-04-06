package br.com.totvs.cia.cadastro.agente.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.cadastro.agente.model.Agente;
import br.com.totvs.cia.cadastro.agente.repository.AgenteRepository;

@Service
public class AgenteService {

	@Autowired
	private AgenteRepository agenteRepository;

	public <S extends Agente> S save(final S entity) {
		return this.agenteRepository.save(entity);
	}

	public List<Agente> findAll() {
		return this.agenteRepository.findAll();
	}
	
	public Agente findByCodigo(String codigo) {
		return agenteRepository.findByCodigo(codigo);
	}

	public Agente findOne(final String id) {
		return this.agenteRepository.findOne(id);
	}

	public void delete(final Agente entity) {
		this.agenteRepository.delete(entity);
	}

	public void delete(final Iterable<? extends Agente> entities) {
		this.agenteRepository.delete(entities);
	}
	
}
