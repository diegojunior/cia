package br.com.totvs.cia.conciliacao.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.model.StatusConciliacaoEnum;

public class ConciliacaoSpecification {

	public static Specification<Conciliacao> search(final Date dataPosicao, final String perfil, final StatusConciliacaoEnum status) {
		return new Specification<Conciliacao>() {

			@Override
			public Predicate toPredicate(final Root<Conciliacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				if (dataPosicao != null) {
					predicates.add(builder.equal(root.<Date>get("data"), dataPosicao));
			    }
		        if (perfil != null && !"".equals(perfil)) {
		        	predicates.add(builder.like(builder.upper(root.<String>get("perfil").get("codigo")), "%" + perfil.toUpperCase() + "%"));
		        }
		        if (status != null) {
					predicates.add(builder.equal(root.<StatusConciliacaoEnum>get("status"), status));
		        }
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<Conciliacao> getPorImportacao(final String idImportacao) {
		return new Specification<Conciliacao>() {
			
			@Override
			public Predicate toPredicate(final Root<Conciliacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (idImportacao != null && !"".equals(idImportacao)) {
					predicates.add(builder.equal(root.<String>get("importacao").get("id"), idImportacao));
				}
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
}
