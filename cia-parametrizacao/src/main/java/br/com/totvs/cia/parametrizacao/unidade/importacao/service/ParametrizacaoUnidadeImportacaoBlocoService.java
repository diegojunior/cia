package br.com.totvs.cia.parametrizacao.unidade.importacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.infra.exception.CiaBusinessException;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.ParametrizacaoUnidadeImportacaoBlocoRepository;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.ParametrizacaoUnidadeImportacaoBlocoSpecification;

@Service
public class ParametrizacaoUnidadeImportacaoBlocoService {

	@Autowired
	private ParametrizacaoUnidadeImportacaoBlocoRepository repository;

	public List<ParametrizacaoUnidadeImportacaoBloco> listBy(final String codigo, final String descricao, final String codigoLayout) {
		return this.repository.findAll(ParametrizacaoUnidadeImportacaoBlocoSpecification.findBy(codigo, descricao, codigoLayout));
	}

	public List<ParametrizacaoUnidadeImportacaoBloco> listByLayout(final String idLayout) {
		return this.repository.findByLayout(idLayout);
	}
	
	public ParametrizacaoUnidadeImportacaoBloco getByLayout(final String idLayout) {
		List<ParametrizacaoUnidadeImportacaoBloco> parametrizacaoUnidadeImportacaoBlocos = this.listByLayout(idLayout);
		if (parametrizacaoUnidadeImportacaoBlocos.isEmpty())
			return null;
		return parametrizacaoUnidadeImportacaoBlocos.iterator().next();
	}
	
	public ParametrizacaoUnidadeImportacaoBloco getBy(final String codigo) {
		return this.repository.getByCodigo(codigo);
	}
	
	public ParametrizacaoUnidadeImportacaoBloco findOne (final String id) {
		return this.repository.findOne(id);
	}
	
	public ParametrizacaoUnidadeImportacaoBloco findOneFetchLinhas (final String id) {
		return this.repository.findOneFetchLinhas(id);
	}

	public ParametrizacaoUnidadeImportacaoBloco incluir(final ParametrizacaoUnidadeImportacaoBloco model) {
		this.verificarUnidadeDuplicada(model);
		return this.repository.save(model);
	}

	public ParametrizacaoUnidadeImportacaoBloco alterar(final ParametrizacaoUnidadeImportacaoBloco model) {
		return this.repository.save(model);
	}

	public void delete(final Iterable<ParametrizacaoUnidadeImportacaoBloco> entities) {
		try {
			for (final ParametrizacaoUnidadeImportacaoBloco param : entities) {
				param.clearLinhas();
			}
			this.repository.delete(entities);
		
		} catch (final DataIntegrityViolationException e) {
			throw new CiaBusinessException("Não foi possivel remover a parametrização de unidade. Há dependência desta parametrização.", e);
		}
	}

	public void deleteAll() {
		this.repository.deleteAll();
	}

	public void verificarUnidadeDuplicada(final ParametrizacaoUnidadeImportacaoBloco model) {
		ParametrizacaoUnidadeImportacaoBloco unidadePorCodigo = this.repository.getByCodigo(model.getCodigo());
		if (unidadePorCodigo != null) {
			throw new CiaBusinessException("Chave(s) Duplicada(s).");
		}
		List<ParametrizacaoUnidadeImportacaoBloco> unidadesPorLayout = this.repository.findByLayout(model.getLayout().getId());
		if (unidadesPorLayout != null && !unidadesPorLayout.isEmpty()) {
			throw new CiaBusinessException("Chave(s) Duplicada(s).");
		}
	}
}