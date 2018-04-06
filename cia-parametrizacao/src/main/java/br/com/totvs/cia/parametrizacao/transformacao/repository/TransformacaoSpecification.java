package br.com.totvs.cia.parametrizacao.transformacao.repository;

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

import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.transformacao.model.TipoTransformacaoEnum;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;

public class TransformacaoSpecification {

	public static Specification<Transformacao> findBy(final TipoLayoutEnum tipoLayoutEnum, final String layout, 
			final String sessao, final String campo, final TipoTransformacaoEnum tipoTransformacaoEnum) {

		return new Specification<Transformacao>() {

			@Override
			@SuppressWarnings("unchecked")
			public Predicate toPredicate(final Root<Transformacao> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (tipoLayoutEnum != null) {
					predicates.add(builder.equal(root.<String>get("tipoLayout"), tipoLayoutEnum));
				}
				if (tipoTransformacaoEnum != null) {
					predicates.add(builder.equal(root.<String>get("tipoTransformacao"), tipoTransformacaoEnum));
				}
				if (layout != null && !"".equals(layout)) {
					final Fetch<Transformacao, Layout> fetchLayout = root.fetch("layout", JoinType.INNER);
					final Join<Transformacao, Layout> joinLayout = (Join<Transformacao, Layout>) fetchLayout;
					predicates.add(builder.like(builder.upper(joinLayout.<String>get("codigo")), "%" + layout.toUpperCase() + "%"));
				}
				if (sessao != null && !"".equals(sessao)) {
					final Fetch<Transformacao, Sessao> fetchSessao = root.fetch("sessao", JoinType.INNER);
					final Join<Transformacao, Sessao> joinSessao = (Join<Transformacao, Sessao>) fetchSessao;
					predicates.add(builder.like(builder.upper(joinSessao.<String>get("codigo")), "%" + sessao.toUpperCase() + "%"));
				}
				if (campo != null && !"".equals(campo)) {
					final Fetch<Transformacao, Campo> fetchCampo = root.fetch("campo", JoinType.INNER);
					final Join<Transformacao, Campo> joinCampo = (Join<Transformacao, Campo>) fetchCampo;
					
					final Fetch<Campo, Dominio> fetchDominio = joinCampo.fetch("dominio", JoinType.INNER);
					final Join<Campo, Dominio> joinDominio = (Join<Campo, Dominio>) fetchDominio;
					predicates.add(builder.like(builder.upper(joinDominio.<String>get("codigo")), "%" + campo.toUpperCase() + "%"));
				}

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
	
	public static Specification<Transformacao> findBy(final TipoLayoutEnum tipoLayoutEnum, final String layout) {

		return new Specification<Transformacao>() {

			@Override
			@SuppressWarnings("unchecked")
			public Predicate toPredicate(final Root<Transformacao> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();

				final Fetch<Transformacao, Layout> fetchLayout = root.fetch("layout", JoinType.INNER);
				final Join<Transformacao, Layout> joinLayout = (Join<Transformacao, Layout>) fetchLayout;
				
				predicates.add(builder.equal(root.<String>get("tipoLayout"), tipoLayoutEnum));
				predicates.add(builder.like(builder.upper(joinLayout.<String>get("codigo")), layout.toUpperCase()));

				
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
	
	public static Specification<Transformacao> findBy(final Transformacao transformacao) {

		return new Specification<Transformacao>() {

			@Override
			@SuppressWarnings("unchecked")
			public Predicate toPredicate(final Root<Transformacao> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				final Fetch<Transformacao, Layout> fetchLayout = root.fetch("layout", JoinType.INNER);
				final Join<Transformacao, Layout> joinLayout = (Join<Transformacao, Layout>) fetchLayout;
				
				final Fetch<Transformacao, Sessao> fetchSessao = root.fetch("sessao", JoinType.INNER);
				final Join<Transformacao, Sessao> joinSessao = (Join<Transformacao, Sessao>) fetchSessao;
				
				final Fetch<Transformacao, Campo> fetchCampo = root.fetch("campo", JoinType.INNER);
				final Join<Transformacao, Campo> joinCampo = (Join<Transformacao, Campo>) fetchCampo;
				
				final Fetch<Campo, Dominio> fetchDominio = joinCampo.fetch("dominio", JoinType.INNER);
				final Join<Campo, Dominio> joinDominio = (Join<Campo, Dominio>) fetchDominio;
				
				predicates.add(builder.equal(root.<String>get("tipoLayout"), transformacao.getTipoLayout()));
				
				predicates.add(builder.equal(joinLayout.<String>get("id"), transformacao.getLayout().getId()));
				
				predicates.add(builder.equal(joinSessao.<String>get("id"), transformacao.getSessao().getId()));
				
				predicates.add(builder.equal(joinDominio.<String>get("id"), transformacao.getCampo().getDominio().getId()));

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
}