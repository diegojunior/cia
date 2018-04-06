package br.com.totvs.cia.importacao.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.cadastro.agente.model.Agente;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.notificacao.model.NotificacaoImportacao;
import br.com.totvs.cia.notificacao.model.StatusEnum;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;


public class ImportacaoSpecification {

	public static Specification<Importacao> listBy(final Date data, final SistemaEnum sistema, final String corretora, final TipoLayoutEnum tipoLayout, final String layout) {
		
		return new Specification<Importacao>() {
			
			@Override
			public Predicate toPredicate(final Root<Importacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (data != null) {
					predicates.add(builder.equal(root.<Date>get("dataImportacao"), data));
				}
				
				if (sistema != null) {
					predicates.add(builder.equal(root.<SistemaEnum>get("sistema"), sistema));
				}
				
				if (corretora != null && !"".equals(corretora)) {
					final Join<Importacao, Agente> joinCorretora = root.join("agente");
					predicates.add(builder.like(builder.upper(joinCorretora.get("codigo")), "%" + corretora.toUpperCase() + "%"));
				}
				
				if (tipoLayout != null) {
					final Join<Importacao, Layout> joinLayout = root.join("layout");
					predicates.add(builder.equal(joinLayout.<TipoLayoutEnum>get("tipoLayout"), tipoLayout));
				}
				
				if (layout != null && !"".equals(layout)) {
					final Join<Importacao, Layout> joinLayout = root.join("layout");
					predicates.add(builder.like(builder.upper(joinLayout.get("codigo")), "%" + layout.toUpperCase() + "%"));
				}
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<Importacao> listByAgenteImportacao(final String codigoAgente) {
		
		return new Specification<Importacao>() {
			
			@Override
			public Predicate toPredicate(final Root<Importacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (codigoAgente != null) {
					final Join<Importacao, Agente> agenteJoin = root.join("agente");
					predicates.add(builder.equal(agenteJoin.<String>get("codigo"), codigoAgente));
				}
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<Importacao> listByLayoutImportacao(final Layout layout) {
		
		return new Specification<Importacao>() {
			
			@Override
			public Predicate toPredicate(final Root<Importacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (layout != null) {
					final Join<Importacao, Layout> layoutJoin = root.join("layout");
					predicates.add(builder.equal(layoutJoin.<String>get("codigo"), layout.getCodigo()));
				}
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<Importacao> listByDataCodigoAgenteLayoutStatusConcluido(final Date data, final String codigoAgente, final Layout layout) {
		
		return new Specification<Importacao>() {
			
			@Override
			public Predicate toPredicate(final Root<Importacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				final Subquery<Importacao> subquery = query.subquery(Importacao.class);
				final Root<NotificacaoImportacao> notificacao = subquery.from(NotificacaoImportacao.class);
				final Join<NotificacaoImportacao, Importacao> join = notificacao.join("importacao");
				
				subquery.select(join);
				
				if (data != null) {
					predicates.add(builder.equal(join.<Date>get("dataImportacao"), data));
				}
				
				if (codigoAgente != null) {
					final Join<Importacao, Agente> agenteJoin = root.join("agente");
					predicates.add(builder.equal(agenteJoin.<String>get("codigo"), codigoAgente));
				}
				
				if (layout != null) {
					final Join<Importacao, Layout> layoutJoin = join.join("layout");
					predicates.add(builder.equal(layoutJoin.<String>get("codigo"), layout.getCodigo()));
				}
				
				predicates.add(builder.equal(notificacao.get("status"), StatusEnum.CONCLUIDO));
				
				subquery.where(predicates.toArray(new Predicate[]{}));
				
				return builder.in(root).value(subquery);
			}
		};
	}

	public static Specification<Importacao> getBy(final Date data, final SistemaEnum sistema, 
			final Layout layout, final Agente agente) {
		return new Specification<Importacao>() {
					
			@Override
			public Predicate toPredicate(final Root<Importacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				predicates.add(builder.equal(root.<Date>get("dataImportacao"), data));
				
				predicates.add(builder.equal(root.<SistemaEnum>get("sistema"), sistema));
				
				predicates.add(builder.equal(root.<Layout>get("layout").get("id"), layout.getId()));
				
				if (agente != null) {
					
					predicates.add(builder.equal(root.<Layout>get("agente").get("idLegado"), agente.getIdLegado()));
					
				}
				
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
}
