package br.com.totvs.cia.carga.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Fetch;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.carga.model.LoteCliente;

public class LoteCargaSpecification {
	
	public static Specification<LoteCarga> findLoteToProcessWithLoteCarteiraBy(final String idCarga) {
		
		return new Specification<LoteCarga>() {
			
			@Override
			@SuppressWarnings("unchecked")
			public Predicate toPredicate(final Root<LoteCarga> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				final List<Predicate> predicates = new ArrayList<Predicate>();
				
				query.distinct(true);
				
				final Fetch<LoteCarga, LoteCliente> fetch = root.fetch("lotesClientes", JoinType.LEFT);
				final Join<LoteCarga, LoteCliente> join = (Join<LoteCarga, LoteCliente>) fetch;
				
				predicates.add(builder.equal(join.getParent().get("carga").get("id"), idCarga));
				predicates.add(builder.equal(root.get("isExecucaoAtivada"), true));

				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
	
	public static Specification<LoteCarga> findBy(final Date dataCarga, final SistemaEnum sistema, final String codigoServico) {
		
		return new Specification<LoteCarga>() {
			
			@Override
			public Predicate toPredicate(final Root<LoteCarga> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {
				
				final List<Predicate> predicates = new ArrayList<Predicate>();
				final Join<LoteCarga, Carga> cargaJoin = root.join("carga");
				
				if (dataCarga != null) {
					predicates.add(builder.equal(cargaJoin.<Date>get("data"), dataCarga));
				}
				if (sistema != null) {
					predicates.add(builder.equal(cargaJoin.<SistemaEnum>get("sistema"), sistema));
		        }
				if (codigoServico != null) {
					final Join<LoteCarga, ConfiguracaoServico> servicoJoin = root.join("servico");
					predicates.add(builder.equal(servicoJoin.<ServicoEnum>get("codigo"), codigoServico));
		        }
				
				return builder.and(predicates.toArray(new Predicate[]{}));
			}
		};
	}
}