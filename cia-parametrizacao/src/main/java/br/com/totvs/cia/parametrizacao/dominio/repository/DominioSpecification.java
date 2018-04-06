package br.com.totvs.cia.parametrizacao.dominio.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.cadastro.tipo.TipoValorDominioEnum;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;

public class DominioSpecification {

	public static Specification<Dominio> findBy(final String codigo, final TipoValorDominioEnum tipo) {

		return new Specification<Dominio>() {

			@Override
			public Predicate toPredicate(final Root<Dominio> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();

				query.distinct(true);

				if (codigo != null && !"".equals(codigo)) {
					predicates.add(builder.like(builder.upper(root.<String>get("codigo")), "%" + codigo.toUpperCase() + "%"));
				}

				if (tipo != null) {
					predicates.add(builder.equal(root.<TipoValorDominioEnum>get("tipo"), tipo));
				}

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

	public static Specification<Dominio> findByCodigo(final String codigo) {

		return new Specification<Dominio>() {

			@Override
			public Predicate toPredicate(final Root<Dominio> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();

				if (codigo != null &&  !"".equals(codigo)) {

					predicates.add(
							builder.like(builder.lower(root.<String>get("codigo")), "%" + codigo.toLowerCase() + "%"));

				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

	public static Specification<Dominio> findByEqualsCodigo(final String codigo) {

		return new Specification<Dominio>() {

			@Override
			public Predicate toPredicate(final Root<Dominio> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();

				if (codigo != null &&  !"".equals(codigo)) {
					predicates.add(
							builder.or(builder.equal(builder.lower(root.<String>get("codigo")), codigo.toLowerCase())));
				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
}