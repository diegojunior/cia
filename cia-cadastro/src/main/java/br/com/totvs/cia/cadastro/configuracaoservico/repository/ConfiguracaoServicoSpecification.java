package br.com.totvs.cia.cadastro.configuracaoservico.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.TipoServicoEnum;

public class ConfiguracaoServicoSpecification {

	public static Specification<ConfiguracaoServico> search(final String codigo, final String descricao, 
			final SistemaEnum sistema, final ServicoEnum servico, final TipoServicoEnum tipoServico) {
		
		return new Specification<ConfiguracaoServico>() {

			@Override
			public Predicate toPredicate(final Root<ConfiguracaoServico> root,
					final CriteriaQuery<?> query, final CriteriaBuilder builder) {

				List<Predicate> predicates = new ArrayList<Predicate>();

				if (codigo != null && !"".equals(codigo)) {
					predicates.add(builder.like(builder.upper(root.<String> get("codigo")), "%"+ codigo.toUpperCase() +"%"));
				}
				
				if (descricao != null && !"".equals(descricao)) {
					predicates.add(builder.like(builder.upper(root.<String> get("descricao")), "%"+ descricao.toUpperCase() +"%"));
				}
				
				if (sistema != null) {
					predicates.add(builder.and(builder.equal(root.get("sistema"), sistema)));
				}
				
				if (servico != null) {
					predicates.add(builder.and(builder.equal(root.get("servico"), servico)));
				}
				
				if (tipoServico != null) {
					predicates.add(builder.and(builder.equal(root.get("tipoServico"), tipoServico)));
				}

				return builder.and(predicates.toArray(new Predicate[] {}));
			}
		};
	}
}