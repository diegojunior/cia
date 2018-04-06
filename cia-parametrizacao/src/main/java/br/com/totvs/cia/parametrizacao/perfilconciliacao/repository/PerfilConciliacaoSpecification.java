package br.com.totvs.cia.parametrizacao.perfilconciliacao.repository;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.ConfiguracaoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.StatusPerfilEnum;

public class PerfilConciliacaoSpecification {
	
	public static Specification<PerfilConciliacao> search(final String codigo, final String descricao, final SistemaEnum sistema, 
			final TipoLayoutEnum tipoLayout, final String layout, final StatusPerfilEnum status) {
		return new Specification<PerfilConciliacao>() {
			@Override
			@SuppressWarnings("unchecked")
			public Predicate toPredicate(final Root<PerfilConciliacao> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				if (codigo != null && !"".equals(codigo.trim())) {
					predicates.add(builder.or(builder.like(builder.upper(root.<String> get("codigo")), "%"+ codigo.toUpperCase().trim() + "%")));
				}
				if (descricao != null && !"".equals(descricao.trim())) {
					predicates.add(builder.or(builder.like(builder.upper(root.<String> get("descricao")), "%"+ descricao.toUpperCase().trim() + "%")));
				}
				if (sistema != null) {
					predicates.add(builder.equal(root.<SistemaEnum>get("sistema"), sistema));
				}
				if (tipoLayout != null) {
					final Fetch<PerfilConciliacao, Layout> fetchLayout = root.fetch("layout", JoinType.INNER);
					final Join<PerfilConciliacao, Layout> joinLayout = (Join<PerfilConciliacao, Layout>) fetchLayout;
					predicates.add(builder.equal(joinLayout.<TipoLayoutEnum>get("tipoLayout"), tipoLayout));
				}
				if (layout != null && !"".equals(layout.trim())) {
					final Fetch<PerfilConciliacao, Layout> fetchLayout = root.fetch("layout", JoinType.INNER);
					final Join<PerfilConciliacao, Layout> joinLayout = (Join<PerfilConciliacao, Layout>) fetchLayout;
					predicates.add(builder.like(builder.upper(joinLayout.<String>get("codigo")), "%" + layout.toUpperCase() + "%"));
				}				
				if (status != null) {
					predicates.add(builder.equal(root.<StatusPerfilEnum>get("status"), status));
		        }
				
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
	
	public static Specification<PerfilConciliacao> findBy(final String codigo) {
		return new Specification<PerfilConciliacao>() {
			@Override
			public Predicate toPredicate(final Root<PerfilConciliacao> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				predicates.add(builder.or(builder.like(builder.upper(root.<String> get("codigo")), "%"+ codigo.toUpperCase().trim() + "%")));
				
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
	
	public static Specification<PerfilConciliacao> findBy(final TipoLayoutEnum tipoLayoutEnum) {
		return new Specification<PerfilConciliacao>() {
			@Override
			public Predicate toPredicate(final Root<PerfilConciliacao> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				if (tipoLayoutEnum != null) {
					predicates.add(builder.equal(root.get("layout").get("tipoLayout"), tipoLayoutEnum));
				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
		
	}
	
	public static Specification<PerfilConciliacao> findBy(final StatusPerfilEnum statusPerfilEnum) {
		return new Specification<PerfilConciliacao>() {
			@Override
			public Predicate toPredicate(final Root<PerfilConciliacao> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				if (statusPerfilEnum != null) {
					predicates.add(builder.equal(root.get("statusPerfil"), statusPerfilEnum));
				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
	
	public static Specification<PerfilConciliacao> findBy(final List<ConfiguracaoServico> configuracaoServico) {
		return new Specification<PerfilConciliacao>() {
			@Override
			public Predicate toPredicate(final Root<PerfilConciliacao> root, final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				if (configuracaoServico != null) {
					Join<PerfilConciliacao, ConfiguracaoPerfilConciliacao> sessoesConciliacao = root.join("sessoesConciliacao");
					predicates.add(builder.and(sessoesConciliacao.get("servicoConciliacao").in(configuracaoServico)));
				}
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
		
	}
}