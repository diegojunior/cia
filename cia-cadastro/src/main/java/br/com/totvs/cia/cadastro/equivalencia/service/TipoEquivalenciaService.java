package br.com.totvs.cia.cadastro.equivalencia.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.equivalencia.model.TipoEquivalencia;
import br.com.totvs.cia.cadastro.equivalencia.repository.TipoEquivalenciaRepository;
import br.com.totvs.cia.cadastro.equivalencia.repository.TipoEquivalenciaSpecification;

@Service
public class TipoEquivalenciaService {

	@Autowired
	private TipoEquivalenciaRepository tipoEquivalenciaRepository;

	public <S extends TipoEquivalencia> S save(final S entity) {
		return this.tipoEquivalenciaRepository.save(entity);
	}

	public List<TipoEquivalencia> findAll() {
		return this.tipoEquivalenciaRepository.findAll();
	}

	public TipoEquivalencia findOne(final String id) {
		return this.tipoEquivalenciaRepository.findOne(id);
	}
	
	public TipoEquivalencia findBy(final TipoEquivalencia tipo) {
		List<TipoEquivalencia> tipos = this.tipoEquivalenciaRepository.findAll(TipoEquivalenciaSpecification.findBy(tipo));
		return tipos.isEmpty() ? null : tipos.iterator().next();
	}

	public void delete(final TipoEquivalencia entity) {
		this.tipoEquivalenciaRepository.delete(entity);
	}

	public void delete(final Iterable<? extends TipoEquivalencia> entities) {
		this.tipoEquivalenciaRepository.delete(entities);
	}

	public TipoEquivalencia getOneBy(final String codigo) {
		List<TipoEquivalencia> tipos = this.tipoEquivalenciaRepository.findAll(TipoEquivalenciaSpecification.findBy(codigo));
		return tipos.isEmpty() ? null : tipos.iterator().next();
	}

	public void saveAll(List<TipoEquivalencia> tipos) {
		for (TipoEquivalencia tipo : tipos) {
			TipoEquivalencia tipoEncontrato = this.findBy(tipo);
			if (tipoEncontrato == null) {
				this.tipoEquivalenciaRepository.save(tipo);
			}else {
				tipo.setId(tipoEncontrato.getId());
			}
		}
		
	}

	public List<TipoEquivalencia> carregaExistentes(List<TipoEquivalencia> tipos) {
		List<TipoEquivalencia> tiposEncontrados = Lists.newArrayList();
		for (TipoEquivalencia tipo : tipos) {
			TipoEquivalencia tipoEncontrado = getOneBy(tipo.getCodigo());
			if (tipoEncontrado == null) {
				tiposEncontrados.add(tipo);
			}else {
				tiposEncontrados.add(tipoEncontrado);
			}
		}
		return tiposEncontrados;
	}
}
