package br.com.totvs.cia.cadastro.configuracaoservico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;

public interface ConfiguracaoServicoRepository extends JpaRepository<ConfiguracaoServico, String>, JpaSpecificationExecutor<ConfiguracaoServico> {
	
	ConfiguracaoServico getByCodigo (final String codigo);
	
	List<ConfiguracaoServico> findBySistema (final SistemaEnum sistema);
	
	@Query(value = "select servico " +
				   "  from ConfiguracaoServico servico join fetch " +
		           "	   servico.campos campos " +
		           " where servico.id = :id")  
	ConfiguracaoServico findOneFetchCampos(@Param("id") final String id);
	
}
