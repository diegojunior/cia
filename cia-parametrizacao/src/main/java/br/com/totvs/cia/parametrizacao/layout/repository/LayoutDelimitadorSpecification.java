package br.com.totvs.cia.parametrizacao.layout.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;

public class LayoutDelimitadorSpecification {

	public static Specification<LayoutDelimitador> findBy(final String codigo, final String descricao,
			final String codigoIdentificador, final StatusLayoutEnum status) {

		return new Specification<LayoutDelimitador>() {

			@Override
			public Predicate toPredicate(final Root<LayoutDelimitador> root, final CriteriaQuery<?> query,
					final CriteriaBuilder builder) {

				List<Predicate> predicates = new ArrayList<Predicate>();

				if (codigo != null && !"".equals(codigo)) {
					predicates.add(builder.like(builder.upper(root.<String>get("codigo")), "%" + codigo.toUpperCase() + "%"));
				}

				if (descricao != null && !"".equals(descricao)) {
					predicates.add(builder.like(builder.upper(root.<String>get("descricao")), "%" + descricao.toUpperCase() + "%"));
				}

				if (codigoIdentificador != null && !"".equals(codigoIdentificador)) {
					Join<LayoutDelimitador, Sessao> join = root.join("sessoes");
					Path<String> codigoPath = join.get("codigo");
					predicates.add(builder.like(builder.upper(codigoPath), "%" + codigoIdentificador.toUpperCase() + "%"));
				}
				
				if (status != null) {
					predicates.add(builder.equal(root.get("status"), status));
				}
				
				predicates.add(builder.equal(root.<TipoLayoutEnum>get("tipoLayout"), TipoLayoutEnum.DELIMITADOR));

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
}