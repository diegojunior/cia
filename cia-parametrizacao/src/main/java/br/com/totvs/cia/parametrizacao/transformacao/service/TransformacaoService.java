package br.com.totvs.cia.parametrizacao.transformacao.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.infra.exception.CiaBusinessException;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.transformacao.model.TipoTransformacaoEnum;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;
import br.com.totvs.cia.parametrizacao.transformacao.repository.TransformacaoRepository;
import br.com.totvs.cia.parametrizacao.transformacao.repository.TransformacaoSpecification;

@Service
public class TransformacaoService {

	@Autowired
	private TransformacaoRepository transformacaoRepository;
	
	@Autowired
	private ItemTransformacaoService itemTransformacaoService;
	
	public List<Transformacao> getByFilters(final TipoLayoutEnum tipoLayout, final String layout, final String sessao, 
			final String campo, final TipoTransformacaoEnum tipoTransformacao) {
		final List<Transformacao> transformacoes = this.transformacaoRepository.findAll(
				TransformacaoSpecification.findBy(tipoLayout, layout, sessao, campo, tipoTransformacao));

		this.sortByTipoLayoutLayoutSessaoCampo(transformacoes);

		return transformacoes;
	}
	
	public List<Transformacao> getBy(final TipoLayoutEnum tipoLayout, final String layout) {
		final List<Transformacao> transformacoes = this.transformacaoRepository.findAll(
				TransformacaoSpecification.findBy(tipoLayout, layout));
		
		this.sortByTipoLayoutLayoutSessaoCampo(transformacoes);

		return transformacoes;
	}

	public List<Transformacao> findAll() {
		return this.transformacaoRepository.findAll();
	}

	public Transformacao getOne(final String id) {
		return this.transformacaoRepository.getOne(id);
	}
	
	public Transformacao incluir(final Transformacao model) {
		
		final List<Transformacao> transformacoes = this.transformacaoRepository.findAll(TransformacaoSpecification.findBy(model));
		
		final Boolean isTransformacaoExistente = transformacoes != null && transformacoes.size() > 0;
		if(isTransformacaoExistente) {
			throw new CiaBusinessException("Chave(s) Duplicada(s).");
		}
		
		this.itemTransformacaoService.validaDuplicidade(model.getItem());
		
		return this.transformacaoRepository.save(model);
	}
	
	public Transformacao alterar(final Transformacao model) {
		
		final List<Transformacao> transformacoes = this.transformacaoRepository.findAll(TransformacaoSpecification.findBy(model));
		
		for (final Transformacao transformacao : transformacoes) {
			if(!transformacao.getId().equals(model.getId())) {
				throw new CiaBusinessException("Chave(s) Duplicada(s).");
			}
		}
		
		this.itemTransformacaoService.validaDuplicidade(model.getItem());
		
		return this.transformacaoRepository.save(model);
	}

	public void delete(final Iterable<Transformacao> entities) {
		try {
			for (Transformacao transformacao : entities) {
				boolean found = transformacaoRepository.existeImportacaoParaLayout(transformacao.getLayout());
				if (found)
					throw new CiaBusinessException("Não foi possivel remover a transformação. Há dependência desta transformação.");
			}
			this.transformacaoRepository.delete(entities);
		
		} catch (final DataIntegrityViolationException e) {
			throw new CiaBusinessException("Não foi possivel remover a transformação. Há dependência desta transformação.", e);
		}
	}

	public void deleteAll() {
		Boolean found = transformacaoRepository.existeImportacaoParaTransformacao();
		if (found)
			throw new CiaBusinessException("Não foi possivel remover a transformação. Há dependência desta transformação.");
		this.transformacaoRepository.deleteAll();
		
	}
	
	private void sortByTipoLayoutLayoutSessaoCampo(final List<Transformacao> transformacoes) {
		
		Collections.sort(transformacoes, new Comparator<Transformacao>() {
			@Override
			public int compare(final Transformacao t1, final Transformacao t2) {
				
				if (t1.getTipoLayout().compareTo(t2.getTipoLayout()) == 0) {
					
					if (t1.getLayout().getCodigo().compareTo(t2.getLayout().getCodigo()) == 0) {
						
						if (t1.getSessao().getCodigo().compareTo(t2.getSessao().getCodigo()) == 0) {
							
							return t1.getCampo().getDominio().getCodigo().compareTo(
									t2.getCampo().getDominio().getCodigo());
							
						}
						
						return t1.getSessao().getCodigo().compareTo(t2.getSessao().getCodigo());
					}
					
					return t1.getLayout().getCodigo().compareTo(t2.getLayout().getCodigo());
				}
				
				return t1.getTipoLayout().compareTo(t2.getTipoLayout());
			}
		});
		
	}
}
