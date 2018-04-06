package br.com.totvs.cia.cadastro.equivalencia.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.cadastro.equivalencia.model.Remetente;

public class RemetenteSpecification {
	
	public static Specification<Remetente> findBy(final String codigo) {
		
		return new Specification<Remetente>() {
			
			@Override
			public Predicate toPredicate(final Root<Remetente> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (codigo != null &&  !"".equals(codigo)) {
					predicates.add(builder.like(builder.lower(root.<String>get("codigo")), "%" + codigo.toLowerCase() + "%"));
				}
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<Remetente> findBy(final Remetente remetente) {
		
		return new Specification<Remetente>() {
			
			@Override
			public Predicate toPredicate(final Root<Remetente> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (remetente != null && !"".equals(remetente.getIdLegado())) {
					predicates.add(builder.equal(root.<String>get("idLegado"), remetente.getIdLegado() ));
				}
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
}