package br.com.totvs.cia.parametrizacao.dominio.service;

import static br.com.totvs.cia.parametrizacao.dominio.repository.DominioSpecification.findByEqualsCodigo;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.cadastro.tipo.TipoValorDominioEnum;
import br.com.totvs.cia.infra.exception.CiaBusinessException;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.dominio.repository.DominioRepository;
import br.com.totvs.cia.parametrizacao.dominio.repository.DominioSpecification;

@Service
public class DominioService {

	@Autowired
	private DominioRepository dominioRepository;

	public List<Dominio> getByFilters(final String codigo, final TipoValorDominioEnum tipoEnum) {
		final List<Dominio> dominios = this.dominioRepository.findAll(DominioSpecification.findBy(codigo, tipoEnum));
		
		this.sortByCodigo(dominios);
		
		return dominios;
	}

	public Dominio getBy(final String codigo) {
		return this.dominioRepository.findOne(DominioSpecification.findByCodigo(codigo));
	}

	public List<Dominio> findAll() {
		return this.dominioRepository.findAll();
	}

	public Dominio getOne(final String id) {
		return this.dominioRepository.getOne(id);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public Dominio incluir(final Dominio model) {

		final Dominio dominio = this.dominioRepository.findOne(findByEqualsCodigo(model.getCodigo()));

		final Boolean isDominioExistente = dominio != null;
		if (isDominioExistente) {
			throw new CiaBusinessException("Chave(s) Duplicada(s).");
		}

		return this.dominioRepository.save(model);
	}

	public Dominio alterar(final Dominio model) {

		return this.dominioRepository.save(model);
	}

	public void delete(final Iterable<Dominio> entities) {
		try {
			this.dominioRepository.delete(entities);
		} catch (final DataIntegrityViolationException e) {
			throw new CiaBusinessException("Não foi possivel remover o campo. Há dependência deste campo.", e);
		}
	}

	public void deleteAll() {
		try {
			this.dominioRepository.deleteAll();
		} catch (final DataIntegrityViolationException e) {
			throw new CiaBusinessException("Não foi possivel remover o campo. Há dependência deste campo.", e);
		}
	}
	
	//TODO [RENAN] Apos definir o padrao do cadastro do Codigo do Dominio (Maiusculo, Minusculo, Camel Case, etc) revisar esta ordenacao.
	private void sortByCodigo(final List<Dominio> dominios) {
		Collections.sort(dominios, new Comparator<Dominio>() {
			@Override
			public int compare(final Dominio d1, final Dominio d2) {
				return d1.getCodigo().toUpperCase().compareTo(d2.getCodigo().toUpperCase());
			}
		});
	}

}
