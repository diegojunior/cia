package br.com.totvs.cia.cadastro.carteira.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.cadastro.carteira.model.Cliente;

public class CarteiraSpecification {
	
	public static Specification<Cliente> findBy(final String codigo) {
		
		return new Specification<Cliente>() {
			
			@Override
			public Predicate toPredicate(final Root<Cliente> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (codigo != null &&  !"".equals(codigo)) {
					predicates.add(builder.like(builder.lower(root.<String>get("codigo")), "%" + codigo.toLowerCase() + "%"));
				}
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<Cliente> findBy(final Cliente cliente) {
		
		return new Specification<Cliente>() {
			
			@Override
			public Predicate toPredicate(final Root<Cliente> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (cliente != null && !"".equals(cliente.getIdLegado())) {
					predicates.add(builder.equal(root.<String>get("idLegado"), cliente.getIdLegado() ));
				}
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
}