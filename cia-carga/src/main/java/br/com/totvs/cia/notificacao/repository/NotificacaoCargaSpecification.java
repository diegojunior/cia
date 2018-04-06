package br.com.totvs.cia.notificacao.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.notificacao.model.NotificacaoCarga;
import br.com.totvs.cia.notificacao.model.StatusEnum;

public class NotificacaoCargaSpecification {

	public static Specification<NotificacaoCarga> findByLoteCarga(final String idLote) {

		return new Specification<NotificacaoCarga>() {

			@Override
			public Predicate toPredicate(final Root<NotificacaoCarga> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(builder.equal(root.<String>get("loteCarga").get("id"), idLote));
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
	
	public static Specification<NotificacaoCarga> findByLotesCargaAndStatus(final List<LoteCarga> lotesCarga, final StatusEnum status) {

		return new Specification<NotificacaoCarga>() {

			@Override
			public Predicate toPredicate(final Root<NotificacaoCarga> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();
				predicates.add(root.<LoteCarga>get("loteCarga").in(lotesCarga));
				predicates.add(builder.equal(root.<StatusEnum>get("status"), status));
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}

	public static Specification<NotificacaoCarga> findByCargas(final List<Carga> cargas) {
		return new Specification<NotificacaoCarga>() {

			@Override
			public Predicate toPredicate(final Root<NotificacaoCarga> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {

				final List<Predicate> predicates = new ArrayList<Predicate>();
				Join<NotificacaoCarga, LoteCarga> loteCargaJoin = root.join("loteCarga");
				predicates.add(loteCargaJoin.<Carga>get("carga").in(cargas));
				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
}
