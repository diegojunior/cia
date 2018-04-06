package br.com.totvs.cia.parametrizacao.layout.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.infra.exception.CiaBusinessException;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.repository.LayoutPosicionalRepository;
import br.com.totvs.cia.parametrizacao.layout.repository.LayoutPosicionalSpecification;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoChaveService;

@Service
public class LayoutPosicionalService {

	@Autowired
	private LayoutPosicionalRepository layoutPosicionalRepository;
	
	@Autowired
	private ParametrizacaoUnidadeImportacaoChaveService parametrizacaoUnidadeService;

	@Transactional
	public LayoutPosicional save(final LayoutPosicional layout) {
		LayoutPosicional result = this.layoutPosicionalRepository.findByCodigo(layout.getCodigo());

		if (result != null) {
			throw new CiaBusinessException("Chave(s) Duplicada(s).");
		}

		return this.layoutPosicionalRepository.save(layout);
	}

	@Transactional
	public LayoutPosicional update(final LayoutPosicional layout) {

		LayoutPosicional layoutSaved = this.layoutPosicionalRepository.findOne(layout.getId());

		layoutSaved.setStatus(layout.getStatus());

		return this.layoutPosicionalRepository.save(layoutSaved);
	}
	
	public LayoutPosicional ativar(final String id) {
		LayoutPosicional layoutEncontrado = this.layoutPosicionalRepository.findOne(id);
		
		final Boolean isLayoutInexistente = layoutEncontrado == null;
		if(isLayoutInexistente) {
			throw new CiaBusinessException("Layout Posicional não encontrado!");
		}
		
		layoutEncontrado.setStatus(StatusLayoutEnum.ATIVO);
		return this.layoutPosicionalRepository.save(layoutEncontrado);
	}
	
	public LayoutPosicional inativar(final String id) {
		LayoutPosicional layoutEncontrado = this.layoutPosicionalRepository.findOne(id);
		
		final Boolean isLayoutInexistente = layoutEncontrado == null;
		if(isLayoutInexistente) {
			throw new CiaBusinessException("Layout Posicional não encontrado!");
		}
		
		layoutEncontrado.setStatus(StatusLayoutEnum.INATIVO);
		return this.layoutPosicionalRepository.save(layoutEncontrado);
	}

	@Transactional
	public void delete(final Iterable<? extends LayoutPosicional> entities) {
		for (LayoutPosicional layoutPosicional : entities) {
			List<ParametrizacaoUnidadeImportacaoChave> unidades = parametrizacaoUnidadeService.getByLayout(layoutPosicional.getId());
			if (!unidades.isEmpty()) {
				throw new CiaBusinessException("Não foi possivel remover o layout. Há dependência deste layout.");
			}
		}
		this.layoutPosicionalRepository.delete(entities);
	}

	@Transactional
	public void deleteAll() {
		try {
			this.layoutPosicionalRepository.deleteAll();
		} catch (Exception e) {
			throw new CiaBusinessException("Não foi possivel remover o layout. Há dependência deste layout.", e);
		}
	}

	public List<LayoutPosicional> findAll(final StatusLayoutEnum status) {
		return this.layoutPosicionalRepository.findByStatus(status);
	}

	public LayoutPosicional findOne(final String id) {
		return this.layoutPosicionalRepository.findOne(id);
	}

	public List<LayoutPosicional> findAll(final Iterable<String> ids) {
		return this.layoutPosicionalRepository.findAll(ids);
	}

	public List<LayoutPosicional> findAll(final String codigo, final String descricao, final String codigoIdentificador,
			final StatusLayoutEnum status) {
		return this.layoutPosicionalRepository
				.findAll(LayoutPosicionalSpecification.findBy(codigo, descricao, codigoIdentificador, status));
	}

}
