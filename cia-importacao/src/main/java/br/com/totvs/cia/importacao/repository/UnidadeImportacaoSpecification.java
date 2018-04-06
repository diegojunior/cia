package br.com.totvs.cia.importacao.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.UnidadeImportacao;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.AbstractParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;

public class UnidadeImportacaoSpecification {

	public static Specification<UnidadeImportacao> listByImportacao(final Importacao importacao) {
		
		return new Specification<UnidadeImportacao>() {
			
			@Override
			public Predicate toPredicate(final Root<UnidadeImportacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				final Join<UnidadeImportacao, Importacao> importacaoJoin = root.join("importacao");
				predicates.add(builder.equal(importacaoJoin.get("id"), importacao.getId()));
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<UnidadeImportacao> listByImportacaoSessaoOrParametrizacao(final Importacao importacao, 
			final Sessao sessao, 
			final AbstractParametrizacaoUnidadeImportacao parametrizacao) {
		
		return new Specification<UnidadeImportacao>() {
			
			@Override
			public Predicate toPredicate(final Root<UnidadeImportacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				final Join<UnidadeImportacao, Importacao> importacaoJoin = root.join("importacao");
				predicates.add(builder.equal(importacaoJoin.get("id"), importacao.getId()));
				
				if (sessao != null 
						&& !sessao.isSemSessaoConfigurada()) {
					final Join<UnidadeImportacao, Sessao> sessaoJoin = root.join("sessao");
					predicates.add(builder.equal(sessaoJoin.get("id"), sessao.getId()));
				}
				
				if (parametrizacao != null) {
					if (parametrizacao instanceof ParametrizacaoUnidadeImportacaoChave) {
						final Join<UnidadeImportacao, ParametrizacaoUnidadeImportacaoChave> parametrizacaoJoin = root.join("parametrizacaoUnidade");
						predicates.add(builder.equal(parametrizacaoJoin.get("id"), parametrizacao.getId()));
					} else {
						final Join<UnidadeImportacao, ParametrizacaoUnidadeImportacaoBloco> parametrizacaoJoin = root.join("parametrizacaoBlocoUnidade");
						predicates.add(builder.equal(parametrizacaoJoin.get("id"), parametrizacao.getId()));
					}
				}
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
}
