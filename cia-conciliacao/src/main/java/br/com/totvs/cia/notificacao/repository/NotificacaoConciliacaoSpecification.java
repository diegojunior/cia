package br.com.totvs.cia.notificacao.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.google.common.collect.Lists;

import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.model.StatusConciliacaoEnum;
import br.com.totvs.cia.notificacao.model.NotificacaoConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;

public class NotificacaoConciliacaoSpecification {

	public static Specification<NotificacaoConciliacao> findBy(final List<Conciliacao> conciliacoes) {

		return new Specification<NotificacaoConciliacao>() {

			@Override
			public Predicate toPredicate(final Root<NotificacaoConciliacao> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();
				final Path<Conciliacao> conciliacaoPath = root.<Conciliacao>get("conciliacao");
				predicates.add(conciliacaoPath.in(conciliacoes));
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

	public static Specification<NotificacaoConciliacao> findBy(final Date data, 
			final List<PerfilConciliacao> perfis, 
			final StatusConciliacaoEnum status) {
		return new Specification<NotificacaoConciliacao>() {

			@Override
			public Predicate toPredicate(final Root<NotificacaoConciliacao> root, 
					final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {
				List<Predicate> predicates = Lists.newArrayList();
				
				Join<NotificacaoConciliacao, Conciliacao> conciliacaoJoin = root.join("conciliacao");
				if (data != null) {
					predicates.add(builder.equal(conciliacaoJoin.<Date>get("data"), data));
				}
				if (perfis != null && !perfis.isEmpty()) {
					predicates.add(conciliacaoJoin.<PerfilConciliacao>get("perfil").in(perfis));
				}
				if (status != null) {
					predicates.add(builder.equal(conciliacaoJoin.<StatusConciliacaoEnum>get("status"), status));
				}
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}

}
