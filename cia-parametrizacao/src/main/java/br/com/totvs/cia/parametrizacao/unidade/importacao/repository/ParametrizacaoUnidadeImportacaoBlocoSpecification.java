package br.com.totvs.cia.parametrizacao.unidade.importacao.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;

public class ParametrizacaoUnidadeImportacaoBlocoSpecification {

	public static Specification<ParametrizacaoUnidadeImportacaoBloco> findBy(final String codigo, final String descricao, final String codigoLayout) {

		return new Specification<ParametrizacaoUnidadeImportacaoBloco>() {

			@Override
			@SuppressWarnings("unchecked")
			public Predicate toPredicate(final Root<ParametrizacaoUnidadeImportacaoBloco> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();

				if (!StringUtils.isEmpty(codigo)) {
					predicates.add(builder.like(builder.upper(root.<String>get("codigo")), "%" + codigo.toUpperCase() + "%"));
				}

				if (!StringUtils.isEmpty(descricao)) {
					predicates.add(builder.like(builder.upper(root.<String>get("descricao")), "%" + descricao.toUpperCase() + "%"));
				}
				
				if (!StringUtils.isEmpty(codigoLayout)) {
					final Fetch<ParametrizacaoUnidadeImportacaoBloco, Layout> fetch = root.fetch("layout", JoinType.INNER);
					final Join<ParametrizacaoUnidadeImportacaoBloco, Layout> join = (Join<ParametrizacaoUnidadeImportacaoBloco, Layout>) fetch;

					predicates.add(builder.like(builder.upper(join.<String>get("codigo")), "%" + codigoLayout.toUpperCase() + "%"));
				}				

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
}