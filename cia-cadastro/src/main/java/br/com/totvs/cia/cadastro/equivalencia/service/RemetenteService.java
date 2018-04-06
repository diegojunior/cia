package br.com.totvs.cia.cadastro.equivalencia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.equivalencia.model.Remetente;
import br.com.totvs.cia.cadastro.equivalencia.repository.RemetenteRepository;
import br.com.totvs.cia.cadastro.equivalencia.repository.RemetenteSpecification;

@Service
public class RemetenteService {

	@Autowired
	private RemetenteRepository remetenteRepository;

	public <S extends Remetente> S save(final S entity) {
		return this.remetenteRepository.save(entity);
	}

	public List<Remetente> findAll() {
		return this.remetenteRepository.findAll();
	}

	public Remetente findOne(final String id) {
		return this.remetenteRepository.findOne(id);
	}
	
	public Remetente findBy(final Remetente remetente) {
		List<Remetente> remetentes = this.remetenteRepository.findAll(RemetenteSpecification.findBy(remetente));
		return remetentes.isEmpty() ? null : remetentes.iterator().next();
	}

	public void delete(final Remetente entity) {
		this.remetenteRepository.delete(entity);
	}

	public void delete(final Iterable<? extends Remetente> entities) {
		this.remetenteRepository.delete(entities);
	}

	public Remetente getOneBy(final String codigo) {
		List<Remetente> remetentes = this.remetenteRepository.findAll(RemetenteSpecification.findBy(codigo));
		return remetentes.isEmpty() ? null : remetentes.iterator().next();
	}

	public void saveAll(List<Remetente> remetentes) {
		for (Remetente remetente : remetentes) {
			Remetente remetenteEncontrato = this.findBy(remetente);
			if (remetenteEncontrato == null) {
				this.remetenteRepository.save(remetente);
			}else {
				remetente.setId(remetenteEncontrato.getId());
			}
		}
		
	}

	public List<Remetente> carregaExistentes(List<Remetente> remetentes) {
		List<Remetente> remetentesEncontrados = Lists.newArrayList();
		for (Remetente remetente : remetentes) {
			Remetente remetenteEncontrado = getOneBy(remetente.getCodigo());
			if (remetenteEncontrado == null) {
				remetentesEncontrados.add(remetente);
			}else {
				remetentesEncontrados.add(remetenteEncontrado);
			}
		}
		return remetentesEncontrados;
	}
}
