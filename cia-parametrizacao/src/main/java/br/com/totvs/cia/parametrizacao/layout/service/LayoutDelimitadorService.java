package br.com.totvs.cia.parametrizacao.layout.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.infra.exception.CiaBusinessException;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.repository.LayoutDelimitadorRepository;
import br.com.totvs.cia.parametrizacao.layout.repository.LayoutDelimitadorSpecification;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoBlocoService;

@Service
public class LayoutDelimitadorService {

	@Autowired
	private LayoutDelimitadorRepository layoutDelimitadorRepository;
	
	@Autowired
	private ParametrizacaoUnidadeImportacaoBlocoService parametrizacaoUnidadeImportacaoBlocoService;

	@Transactional
	public LayoutDelimitador save(final LayoutDelimitador layout) {
		LayoutDelimitador result = this.layoutDelimitadorRepository.findByCodigo(layout.getCodigo());
		
		if (result != null) {
			throw new CiaBusinessException("Chave(s) Duplicada(s).");
		}

		return this.layoutDelimitadorRepository.save(layout);
	}

	@Transactional
	public LayoutDelimitador update(final LayoutDelimitador layout) {

		LayoutDelimitador layoutSaved = this.layoutDelimitadorRepository.findOne(layout.getId());

		layoutSaved.setStatus(layout.getStatus());

		return this.layoutDelimitadorRepository.save(layoutSaved);
	}
	
	public LayoutDelimitador ativar(final String id) {
		LayoutDelimitador layoutEncontrado = this.layoutDelimitadorRepository.findOne(id);
		
		final Boolean isLayoutInexistente = layoutEncontrado == null;
		if(isLayoutInexistente) {
			throw new CiaBusinessException("Layout Delimitador não encontrado!");
		}
		
		layoutEncontrado.setStatus(StatusLayoutEnum.ATIVO);
		return this.layoutDelimitadorRepository.save(layoutEncontrado);
	}
	
	public LayoutDelimitador inativar(final String id) {
		LayoutDelimitador layoutEncontrado = this.layoutDelimitadorRepository.findOne(id);
		
		final Boolean isLayoutInexistente = layoutEncontrado == null;
		if(isLayoutInexistente) {
			throw new CiaBusinessException("Layout Delimitador não encontrado!");
		}
		
		layoutEncontrado.setStatus(StatusLayoutEnum.INATIVO);
		return this.layoutDelimitadorRepository.save(layoutEncontrado);
	}
	
	@Transactional
	public void delete(final Iterable<? extends LayoutDelimitador> entities) {
		for (LayoutDelimitador layoutDelimitador : entities) {
			ParametrizacaoUnidadeImportacaoBloco parametrizacaoUnidade = this.parametrizacaoUnidadeImportacaoBlocoService.getByLayout(layoutDelimitador.getId());
			if (parametrizacaoUnidade != null) {
				throw new CiaBusinessException("Não foi possivel remover o layout. Há dependência deste layout.");
			}
		}
		this.layoutDelimitadorRepository.delete(entities);
	}

	@Transactional
	public void deleteAll() {
		try {
			this.layoutDelimitadorRepository.deleteAll();
		} catch (Exception e) {
			throw new CiaBusinessException("Não foi possivel remover o layout. Há dependência deste layout.", e);
		}
	}

	public List<LayoutDelimitador> findAll(final StatusLayoutEnum status) {
		return this.layoutDelimitadorRepository.findByStatus(status);
	}

	public LayoutDelimitador findOne(final String id) {
		return this.layoutDelimitadorRepository.findOne(id);
	}

	public List<LayoutDelimitador> findAll(final Iterable<String> ids) {
		return this.layoutDelimitadorRepository.findAll(ids);
	}

	public List<LayoutDelimitador> findAll(final String codigo, final String descricao, final String codigoIdentificador,
			final StatusLayoutEnum status) {
		List<LayoutDelimitador> layouts = this.layoutDelimitadorRepository.findAll(LayoutDelimitadorSpecification.findBy(codigo, descricao, codigoIdentificador, status));
		return layouts;
	}

}
