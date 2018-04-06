package br.com.totvs.cia.parametrizacao.layout.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;

public class LayoutXmlSpecification {
	
	public static Specification<LayoutXml> findBy(final String codigo, final String descricao, final StatusLayoutEnum status) {

		return new Specification<LayoutXml>() {

			@Override
			public Predicate toPredicate(final Root<LayoutXml> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				List<Predicate> predicates = new ArrayList<Predicate>();

				if (codigo != null && !"".equals(codigo)) {
					predicates.add(builder.like(builder.upper(root.<String>get("codigo")), "%" + codigo.toUpperCase() + "%"));
				}

				if (descricao != null && !"".equals(descricao)) {
					predicates.add(builder.like(builder.upper(root.<String>get("descricao")), "%" + descricao.toUpperCase() + "%"));
				}
				
				if (status != null) {
					predicates.add(builder.equal(root.<String>get("status"), status));
				}

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
}