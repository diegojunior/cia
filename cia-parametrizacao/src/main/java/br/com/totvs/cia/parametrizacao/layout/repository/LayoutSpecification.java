package br.com.totvs.cia.parametrizacao.layout.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.parametrizacao.layout.model.Layout;

public class LayoutSpecification {

	public static Specification<Layout> findBy(final String codigo) {

		return new Specification<Layout>() {

			@Override
			public Predicate toPredicate(final Root<Layout> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				List<Predicate> predicates = new ArrayList<Predicate>();

				if (codigo != null && !"".equals(codigo)) {
					predicates.add(builder.equal(builder.lower(root.<String>get("codigo")), codigo.toLowerCase()));
				}

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
}