package br.com.totvs.cia.notificacao.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.notificacao.model.NotificacaoImportacao;

public class NotificacaoImportacaoSpecification {

	public static Specification<NotificacaoImportacao> findBy(final Importacao importacao) {

		return new Specification<NotificacaoImportacao>() {

			@Override
			public Predicate toPredicate(final Root<NotificacaoImportacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {

				List<Predicate> predicates = new ArrayList<Predicate>();
				Path<Importacao> importacaoPath = root.<Importacao>get("importacao");
				predicates.add(builder.equal(importacaoPath.<Long>get("id"), importacao.getId()));
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
	
	public static Specification<NotificacaoImportacao> findBy(final List<Importacao> importacoes) {

		return new Specification<NotificacaoImportacao>() {

			@Override
			public Predicate toPredicate(final Root<NotificacaoImportacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {

				List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NotificacaoImportacao, Importacao> joinImportacao = root.join("importacao");
				predicates.add(joinImportacao.in(importacoes));
				
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

}
