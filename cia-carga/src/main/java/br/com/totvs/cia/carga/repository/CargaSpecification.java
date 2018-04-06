package br.com.totvs.cia.carga.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.StatusCargaEnum;

public class CargaSpecification {

	public static Specification<Carga> search(final Date data, final SistemaEnum sistema, final StatusCargaEnum status) {
		return new Specification<Carga>() {
			@Override
			public Predicate toPredicate(final Root<Carga> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (data != null) {
					predicates.add(builder.equal(root.<Date>get("data"), data));
				}
				if (sistema != null) {
					predicates.add(builder.equal(root.<SistemaEnum>get("sistema"), sistema));
		        }
				if (status != null) {
					predicates.add(builder.equal(root.<StatusCargaEnum>get("status"), status));
		        }
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<Carga> getByCodigo(final String codigo) {
		return new Specification<Carga>() {
			@Override
			public Predicate toPredicate(final Root<Carga> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (codigo != null && !"".equals(codigo)) {
					predicates.add(builder.like(root.<String>get("codigo"), "%" + codigo + "%"));
				} else {
					predicates.add(builder.isNull(root.<String>get("codigo")));
				}
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<Carga> getByDataPosicao(final Date data) {
		return new Specification<Carga>() {
			@Override
			public Predicate toPredicate(final Root<Carga> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (data != null) {
					predicates.add(builder.equal(root.<Date>get("data"), data));
				}
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<Carga> getByServicos(final List<ConfiguracaoServico> servicos) {
		return new Specification<Carga>() {
			@Override
			public Predicate toPredicate(final Root<Carga> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				if (servicos != null && !servicos.isEmpty()) {
					final Join<Carga, ConfiguracaoServico> servicosJoin = root.join("servicos");
					final Predicate servicosIn = servicosJoin.in(servicos);
					predicates.add(servicosIn);
				}
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<Carga> getByStatusConcluido() {
		return new Specification<Carga>() {
			@Override
			public Predicate toPredicate(final Root<Carga> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				final List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.<Date>get("status"), StatusCargaEnum.CONCLUIDO));
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
}
