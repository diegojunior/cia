package br.com.totvs.cia.importacao.service;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.UnidadeImportacao;
import br.com.totvs.cia.importacao.repository.UnidadeImportacaoCustomRepository;
import br.com.totvs.cia.importacao.repository.UnidadeImportacaoRepository;
import br.com.totvs.cia.importacao.repository.UnidadeImportacaoSpecification;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.AbstractParametrizacaoUnidadeImportacao;

@Service
public class UnidadeImportacaoService {

	@Autowired
	private UnidadeImportacaoRepository unidadeImportacaoRepository;
	
	@Autowired
	private UnidadeImportacaoCustomRepository repo;

	public <S extends UnidadeImportacao> S save(final S entity) {
		return this.unidadeImportacaoRepository.save(entity);
	}

	public <S extends UnidadeImportacao> List<S> save(final Iterable<S> entities) {
		return this.unidadeImportacaoRepository.save(entities);
	}

	public List<UnidadeImportacao> findAll(final Importacao importacao) {
		return this.unidadeImportacaoRepository.findAll(UnidadeImportacaoSpecification.listByImportacao(importacao));
	}
	
	public List<UnidadeImportacao> findAll(final Importacao importacao, 
			final Sessao sessao, 
			final AbstractParametrizacaoUnidadeImportacao parametrizacao) {
		List<UnidadeImportacao> unidades = this.unidadeImportacaoRepository.findAll(UnidadeImportacaoSpecification.
				listByImportacaoSessaoOrParametrizacao(importacao, 
						sessao, 
						parametrizacao));
		Collections.sort(unidades);
		return unidades;
	}

	public List<UnidadeImportacao> findAll(final Specification<UnidadeImportacao> spec) {
		return this.unidadeImportacaoRepository.findAll(spec);
	}
	
	public long countUnidadesImportadasPor(final Importacao importacao) {
		return this.unidadeImportacaoRepository.count(UnidadeImportacaoSpecification.listByImportacao(importacao));
	}

	public void delete(final Importacao importacao) {
		this.unidadeImportacaoRepository.deletarCamposUnidadesPelaImportacao(importacao);
		this.unidadeImportacaoRepository.deletarUnidadesPelaImportacao(importacao);
	}

	public List<? extends UnidadeImportacao> bulkSave(final List<? extends UnidadeImportacao> entities) {
		return this.repo.bulkSave(entities);
	}
	
	
	
	
	
}
