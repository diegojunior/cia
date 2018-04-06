package br.com.totvs.cia.parametrizacao.validacao.repository;

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

import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.AbstractValidacaoArquivo;
import br.com.totvs.cia.parametrizacao.validacao.model.CampoValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.LocalValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.TipoValidacaoEnum;

public class ValidacaoArquivoSpecification {

	public static Specification<AbstractValidacaoArquivo> findBy(final TipoLayoutEnum tipoLayout, final String layout, 
			final TipoValidacaoEnum tipoValidacao, final CampoValidacaoArquivoEnum campoValidacao, final LocalValidacaoArquivoEnum localValidacao) {
			
		return new Specification<AbstractValidacaoArquivo>() {

			@Override
			@SuppressWarnings("unchecked")
			public Predicate toPredicate(final Root<AbstractValidacaoArquivo> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (tipoLayout != null) {
					predicates.add(builder.equal(root.<String>get("tipoLayout"), tipoLayout));
				}

				if (layout != null && !"".equals(layout)) {
					
					final Fetch<AbstractValidacaoArquivo, Layout> fetch = root.fetch("layout", JoinType.INNER);
					final Join<AbstractValidacaoArquivo, Layout> join = (Join<AbstractValidacaoArquivo, Layout>) fetch;
					
					predicates.add(builder.like(builder.upper(join.<String>get("codigo")), "%" + layout.toUpperCase() + "%"));
				}
				
				if (tipoValidacao != null) {
					predicates.add(builder.equal(root.<String>get("tipoValidacao"), tipoValidacao));
				}
				
				if (campoValidacao != null) {
					predicates.add(builder.equal(root.<String>get("campoValidacao"), campoValidacao));
				}
				
				if (localValidacao != null) {
					predicates.add(builder.equal(root.<String>get("localValidacao"), localValidacao));
				}
				
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
	
	public static Specification<AbstractValidacaoArquivo> findBy(final TipoLayoutEnum tipoLayout, final Layout layout) {
			
		return new Specification<AbstractValidacaoArquivo>() {

			@Override
			public Predicate toPredicate(final Root<AbstractValidacaoArquivo> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				Join<AbstractValidacaoArquivo, Layout> joinLayout = root.join("layout");
				
				predicates.add(builder.equal(root.<String>get("tipoLayout"), tipoLayout));
				predicates.add(builder.equal(joinLayout.get("id"), layout.getId()));
				
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
}