package br.com.totvs.cia.parametrizacao.unidade.importacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ChaveParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.ChaveParametrizacaoUnidadeImportacaoRepository;

@Service
public class ChaveParametrizacaoUnidadeImportacaoService {

	@Autowired
	private ChaveParametrizacaoUnidadeImportacaoRepository campoRepository;

	public void delete(final ChaveParametrizacaoUnidadeImportacao chave) {
		this.campoRepository.delete(chave);
	}

	public void delete(final Iterable<? extends ChaveParametrizacaoUnidadeImportacao> chaves) {
		this.campoRepository.delete(chaves);
	}

	public List<ChaveParametrizacaoUnidadeImportacao> findAll(final Specification<ChaveParametrizacaoUnidadeImportacao> spec) {
		return this.campoRepository.findAll(spec);
	}

	public List<ChaveParametrizacaoUnidadeImportacao> findAll() {
		return this.campoRepository.findAll();
	}

	public ChaveParametrizacaoUnidadeImportacao findOne(final String id) {
		return this.campoRepository.findOne(id);
	}

	public <S extends ChaveParametrizacaoUnidadeImportacao> S save(final S chaves) {
		return this.campoRepository.save(chaves);
	}
}
