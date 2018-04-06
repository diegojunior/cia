package br.com.totvs.cia.cadastro.grupo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.grupo.model.Grupo;
import br.com.totvs.cia.cadastro.grupo.repository.GrupoRepository;
import br.com.totvs.cia.cadastro.grupo.repository.GrupoSpecification;

@Service
public class GrupoService {

	@Autowired
	private GrupoRepository grupoRepository;

	public <S extends Grupo> S save(final S entity) {
		return this.grupoRepository.save(entity);
	}

	public List<Grupo> findAll() {
		return this.grupoRepository.findAll();
	}

	public Grupo findOne(final String id) {
		return this.grupoRepository.findOne(id);
	}
	
	public Grupo findBy(final Grupo grupo) {
		List<Grupo> grupos = this.grupoRepository.findAll(GrupoSpecification.findBy(grupo));
		return grupos.isEmpty() ? null : grupos.iterator().next();
	}

	public void delete(final Grupo entity) {
		this.grupoRepository.delete(entity);
	}

	public void delete(final Iterable<? extends Grupo> entities) {
		this.grupoRepository.delete(entities);
	}

	public Grupo getOneBy(final String codigo) {
		List<Grupo> grupos = this.grupoRepository.findAll(GrupoSpecification.findBy(codigo));
		return grupos.isEmpty() ? null : grupos.iterator().next();
	}

	public void criaGruposInexistentes(List<Grupo> grupos) {
		for (Grupo grupo : grupos) {
			Grupo grupoEncontrado = this.findBy(grupo);
			if (grupoEncontrado != null) {
				grupo.setId(grupoEncontrado.getId());
			}
			this.grupoRepository.save(grupo);
		}
	}

	public List<Grupo> carrega(List<Grupo> grupos) {
		List<Grupo> gruposEncontrados = Lists.newArrayList();
		for (Grupo grupo : grupos) {
			Grupo grupoEncontrado = getOneBy(grupo.getCodigo());
			if (grupoEncontrado == null) {
				gruposEncontrados.add(grupo);
			}else {
				gruposEncontrados.add(grupoEncontrado);
			}
		}
		return gruposEncontrados;
	}
}