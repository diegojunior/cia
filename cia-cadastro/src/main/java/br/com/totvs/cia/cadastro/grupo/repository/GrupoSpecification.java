package br.com.totvs.cia.cadastro.grupo.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.cadastro.grupo.model.Grupo;

public class GrupoSpecification {
	
	public static Specification<Grupo> findBy(final String codigo) {
		
		return new Specification<Grupo>() {
			
			@Override
			public Predicate toPredicate(final Root<Grupo> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (codigo != null &&  !"".equals(codigo)) {
					predicates.add(builder.like(builder.lower(root.<String>get("codigo")), "%" + codigo.toLowerCase() + "%"));
				}
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<Grupo> findBy(final Grupo grupo) {
		
		return new Specification<Grupo>() {
			
			@Override
			public Predicate toPredicate(final Root<Grupo> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (grupo != null && !"".equals(grupo.getIdLegado())) {
					predicates.add(builder.equal(root.<String>get("idLegado"), grupo.getIdLegado() ));
				}
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
}