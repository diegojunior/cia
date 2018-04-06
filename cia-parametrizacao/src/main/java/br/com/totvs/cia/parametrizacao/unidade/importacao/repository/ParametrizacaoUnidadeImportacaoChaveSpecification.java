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
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;

public class ParametrizacaoUnidadeImportacaoChaveSpecification {

	public static Specification<ParametrizacaoUnidadeImportacaoChave> findBy(final String codigo, final String descricao,
			final TipoLayoutEnum tipoLayoutParam, final String codigoLayout, final String codigoIdentificador) {

		return new Specification<ParametrizacaoUnidadeImportacaoChave>() {

			@SuppressWarnings("unchecked")
			@Override
			public Predicate toPredicate(final Root<ParametrizacaoUnidadeImportacaoChave> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();

				if (!StringUtils.isEmpty(codigo)) {

					predicates.add(builder.or(
							builder.like(builder.lower(root.<String>get("codigo")), "%" + codigo.toLowerCase() + "%"),
							builder.like(builder.upper(root.<String>get("codigo")), "%" + codigo.toUpperCase() + "%")));

				}

				if (!StringUtils.isEmpty(descricao)) {
					predicates.add(builder.or(
							builder.like(builder.lower(root.<String>get("descricao")),
									"%" + descricao.toLowerCase() + "%"),
							builder.like(builder.upper(root.<String>get("descricao")),
									"%" + descricao.toUpperCase() + "%")));
				}
				
				if (tipoLayoutParam != null) {
					predicates.add(builder.or(builder.equal(root.get("tipoLayout"), tipoLayoutParam)));
				}

				if (!StringUtils.isEmpty(codigoLayout)) {

					final Fetch<ParametrizacaoUnidadeImportacaoChave, Layout> fetch = root.fetch("layout", JoinType.INNER);

					final Join<ParametrizacaoUnidadeImportacaoChave, Layout> join = (Join<ParametrizacaoUnidadeImportacaoChave, Layout>) fetch;

					predicates.add(builder.or(
							builder.like(builder.lower(join.<String>get("codigo")),
									"%" + codigoLayout.toLowerCase() + "%"),
							builder.like(builder.upper(join.<String>get("codigo")),
									"%" + codigoLayout.toUpperCase() + "%")));
				}

				if (!StringUtils.isEmpty(codigoIdentificador)) {

					final Fetch<ParametrizacaoUnidadeImportacaoChave, Sessao> fetch = root
							.fetch("sessoes", JoinType.INNER);

					final Join<ParametrizacaoUnidadeImportacaoChave, Sessao> joinSessao = (Join<ParametrizacaoUnidadeImportacaoChave, Sessao>) fetch;

					predicates.add(builder.or(
							builder.like(builder.lower(joinSessao.<String>get("codigo")),
									"%" + codigoIdentificador.toLowerCase() + "%"),
							builder.like(builder.upper(joinSessao.<String>get("codigo")),
									"%" + codigoIdentificador.toUpperCase() + "%")));
				}

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

	public static Specification<ParametrizacaoUnidadeImportacaoChave> findByLayout(final String idLayout) {

		return new Specification<ParametrizacaoUnidadeImportacaoChave>() {

			@Override
			@SuppressWarnings("unchecked")
			public Predicate toPredicate(final Root<ParametrizacaoUnidadeImportacaoChave> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();

				final Fetch<ParametrizacaoUnidadeImportacaoChave, Layout> fetch = root.fetch("layout", JoinType.INNER);

				final Join<ParametrizacaoUnidadeImportacaoChave, Layout> join = (Join<ParametrizacaoUnidadeImportacaoChave, Layout>) fetch;

				predicates.add(builder.equal(join.<String>get("id"), idLayout));

				return builder.and(predicates.toArray(new Predicate[] {}));

			}
		};
	}

}