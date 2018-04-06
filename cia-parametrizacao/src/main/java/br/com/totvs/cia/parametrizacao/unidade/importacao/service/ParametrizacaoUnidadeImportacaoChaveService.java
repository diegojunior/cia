package br.com.totvs.cia.parametrizacao.unidade.importacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.infra.exception.CiaBusinessException;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.ParametrizacaoUnidadeImportacaoChaveRepository;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.ParametrizacaoUnidadeImportacaoChaveSpecification;

@Service
public class ParametrizacaoUnidadeImportacaoChaveService {

	@Autowired
	private ParametrizacaoUnidadeImportacaoChaveRepository repository;

	public List<ParametrizacaoUnidadeImportacaoChave> getBy(final String codigo, final String descricao,
			final TipoLayoutEnum tipoLayoutParam, final String codigoLayout, final String codigoIdentificador) {
		return this.repository.findAll(ParametrizacaoUnidadeImportacaoChaveSpecification.findBy(codigo, descricao,
				tipoLayoutParam, codigoLayout, codigoIdentificador));
	}

	public List<ParametrizacaoUnidadeImportacaoChave> getByLayout(final String layout) {
		return this.repository.findAll(ParametrizacaoUnidadeImportacaoChaveSpecification.findByLayout(layout));
	}
	
	public ParametrizacaoUnidadeImportacaoChave getBy(final String codigo) {
		return this.repository.getByCodigo(codigo);
	}

	public List<ParametrizacaoUnidadeImportacaoChave> findAll() {
		return this.repository.findAll();
	}

	public ParametrizacaoUnidadeImportacaoChave getOne(final String id) {
		return this.repository.getOne(id);
	}

	public ParametrizacaoUnidadeImportacaoChave incluir(final ParametrizacaoUnidadeImportacaoChave model) {

		final List<ParametrizacaoUnidadeImportacaoChave> parametrizacao = this.repository.findByCodigo(model.getCodigo());

		final Boolean isParametrizacaoExistente = !parametrizacao.isEmpty();
		if (isParametrizacaoExistente) {
			throw new CiaBusinessException("Chave(s) Duplicada(s).");
		}

		return this.repository.save(model);
	}

	public ParametrizacaoUnidadeImportacaoChave alterar(final ParametrizacaoUnidadeImportacaoChave model) {

		return this.repository.save(model);

	}

	public void delete(final Iterable<ParametrizacaoUnidadeImportacaoChave> entities) {
		try {
			for (final ParametrizacaoUnidadeImportacaoChave param : entities) {
				param.clearSessoes();
				param.clearChaves();
			}
			this.repository.delete(entities);
		} catch (final DataIntegrityViolationException e) {
			throw new CiaBusinessException("Não foi possivel remover a parametrização de unidade. Há dependência desta parametrização.", e);
		}
	}

	public void deleteAll() {
		this.repository.deleteAll();
	}

	public void verificarUnidadeDuplicada(final ParametrizacaoUnidadeImportacaoChave model) {
		
		ParametrizacaoUnidadeImportacaoChave unidadePorCodigo = this.repository.getByCodigo(model.getCodigo());
		if (unidadePorCodigo != null)
			throw new CiaBusinessException("Chave(s) Duplicada(s).");
		
		ParametrizacaoUnidadeImportacaoChave unidadePorLayout = this.repository.findOne(ParametrizacaoUnidadeImportacaoChaveSpecification.findByLayout(model.getLayout().getId()));
		
		if (unidadePorLayout != null) {
			
			boolean unidadePorLayoutSessao = unidadePorLayout
					.getSessoes()
					.stream()
					.allMatch(sessao -> model.getSessoes().contains(sessao));
			
			if (unidadePorLayoutSessao)
				throw new CiaBusinessException("Existe uma unidade de parametrização com esse layout e estes códigos identificadores.");
		}
	}
}