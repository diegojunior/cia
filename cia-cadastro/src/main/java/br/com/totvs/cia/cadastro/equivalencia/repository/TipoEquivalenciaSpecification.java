package br.com.totvs.cia.cadastro.equivalencia.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.cadastro.equivalencia.model.TipoEquivalencia;

public class TipoEquivalenciaSpecification {
	
	public static Specification<TipoEquivalencia> findBy(final String codigo) {
		
		return new Specification<TipoEquivalencia>() {
			
			@Override
			public Predicate toPredicate(final Root<TipoEquivalencia> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (codigo != null &&  !"".equals(codigo)) {
					predicates.add(builder.like(builder.lower(root.<String>get("codigo")), "%" + codigo.toLowerCase() + "%"));
				}
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<TipoEquivalencia> findBy(final TipoEquivalencia tipo) {
		
		return new Specification<TipoEquivalencia>() {
			
			@Override
			public Predicate toPredicate(final Root<TipoEquivalencia> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (tipo != null && !"".equals(tipo.getIdLegado())) {
					predicates.add(builder.equal(root.<String>get("idLegado"), tipo.getIdLegado() ));
				}
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
}