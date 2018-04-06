package br.com.totvs.cia.importacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.importacao.repository.UnidadeLayoutImportacaoCustomRepository;
import br.com.totvs.cia.importacao.repository.UnidadeLayoutImportacaoRepository;

@Service
public class UnidadeLayoutImportacaoService {

	@Autowired
	private UnidadeLayoutImportacaoRepository unidadeLayoutImportacaoRepository;
	
	@Autowired
	private UnidadeLayoutImportacaoCustomRepository unidadeLayoutImportacaoCustomRepository;

	public <S extends UnidadeLayoutImportacao> S save(final S entity) {
		return this.unidadeLayoutImportacaoRepository.save(entity);
	}

	public <S extends UnidadeLayoutImportacao> Iterable<S> save(final Iterable<S> entities) {
		return this.unidadeLayoutImportacaoRepository.save(entities);
	}

	public List<UnidadeLayoutImportacao> getUnidadesByImportacao(final Importacao importacao, final Pageable pageRequest) {
		return this.unidadeLayoutImportacaoRepository.getUnidadesByImportacao(importacao, pageRequest);
	}
	
	public List<UnidadeLayoutImportacao> getUnidadesPorImportacaoSemParametrizacao(final Importacao importacao, final List<UnidadeLayoutImportacao> unidadesParametrizadas, final Pageable pageRequest) {
		return this.unidadeLayoutImportacaoRepository.getUnidadesPorImportacaoSemParametrizacao(importacao, unidadesParametrizadas, pageRequest);
	}

	public List<? extends UnidadeLayoutImportacao> bulkSave(final List<? extends UnidadeLayoutImportacao> entities) {
		return this.unidadeLayoutImportacaoCustomRepository.bulkSave(entities);
	}

	public List<UnidadeLayoutImportacao> obtemUnidadesLayoutOrdenadaNumeroLinha(final Importacao importacao,
			final Pageable pageRequest) {
		return this.unidadeLayoutImportacaoRepository.obtemUnidadesOrdenadasPeloNumeroLinha(importacao, pageRequest);
	}

	public void removeUnidadesLayoutPor(final Importacao importacaoExistente) {
		this.unidadeLayoutImportacaoRepository.removeCamposLayoutPor(importacaoExistente);
		this.unidadeLayoutImportacaoRepository.removeUnidadeLayoutPorImportacao(importacaoExistente);
	}
}
