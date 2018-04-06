package br.com.totvs.cia.parametrizacao.layout.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.infra.exception.CiaBusinessException;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.repository.LayoutXmlRepository;
import br.com.totvs.cia.parametrizacao.layout.repository.LayoutXmlSpecification;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoChaveService;

@Service
public class LayoutXmlService {

	@Autowired
	private LayoutXmlRepository layoutXmlRepository;
	
	@Autowired
	private ParametrizacaoUnidadeImportacaoChaveService parametrizacaoUnidadeService;

	@Transactional
	public LayoutXml save(final LayoutXml layout) {
		LayoutXml result = this.layoutXmlRepository.findByCodigo(layout.getCodigo());
		if (result != null) {
			throw new CiaBusinessException("Chave(s) Duplicada(s).");
		}
		return this.layoutXmlRepository.save(layout);

	}

	@Transactional
	public LayoutXml update(final LayoutXml layoutJson) {

		LayoutXml layoutSaved = this.layoutXmlRepository.findOne(layoutJson.getId());

		layoutSaved.setStatus(layoutJson.getStatus());

		return this.layoutXmlRepository.save(layoutSaved);

	}
	
	public LayoutXml ativar(final String id) {
		LayoutXml layoutEncontrado = this.layoutXmlRepository.findOne(id);
		
		final Boolean isLayoutInexistente = layoutEncontrado == null;
		if(isLayoutInexistente) {
			throw new CiaBusinessException("Layout XML não encontrado!");
		}
		
		layoutEncontrado.setStatus(StatusLayoutEnum.ATIVO);
		return this.layoutXmlRepository.save(layoutEncontrado);
	}
	
	public LayoutXml inativar(final String id) {
		LayoutXml layoutEncontrado = this.layoutXmlRepository.findOne(id);
		
		final Boolean isLayoutInexistente = layoutEncontrado == null;
		if(isLayoutInexistente) {
			throw new CiaBusinessException("Layout XML não encontrado!");
		}
		
		layoutEncontrado.setStatus(StatusLayoutEnum.INATIVO);
		return this.layoutXmlRepository.save(layoutEncontrado);
	}

	@Transactional
	public void delete(final Iterable<? extends LayoutXml> entities) {
		for (LayoutXml layoutXml : entities) {
			List<ParametrizacaoUnidadeImportacaoChave> unidades = parametrizacaoUnidadeService.getByLayout(layoutXml.getId());
			if (!unidades.isEmpty()) {
				throw new CiaBusinessException("Não foi possivel remover o layout. Há dependência deste layout.");
			}
		}
		this.layoutXmlRepository.delete(entities);
	}

	@Transactional
	public void deleteAll() {
		try {
			this.layoutXmlRepository.deleteAll();
		} catch (final Exception e) {
			throw new CiaBusinessException("Não foi possivel remover o layout. Há dependência deste layout.", e);
		}
	}

	public List<LayoutXml> findAll(StatusLayoutEnum status) {
		return this.layoutXmlRepository.findByStatus(status);
	}

	public LayoutXml findOne(final String id) {
		return this.layoutXmlRepository.findOne(id);
	}

	public List<LayoutXml> findAll(final Iterable<String> ids) {
		return this.layoutXmlRepository.findAll(ids);
	}

	public List<LayoutXml> findAll(final String codigo, final String descricao, final StatusLayoutEnum status, final TipoLayoutEnum tipo) {
		return this.layoutXmlRepository.findAll(LayoutXmlSpecification.findBy(codigo, descricao, status));
	}

}
