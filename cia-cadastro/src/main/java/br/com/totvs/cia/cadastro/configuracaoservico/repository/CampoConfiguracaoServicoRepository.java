package br.com.totvs.cia.cadastro.configuracaoservico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;

public interface CampoConfiguracaoServicoRepository extends JpaRepository<CampoConfiguracaoServico, String>, JpaSpecificationExecutor<CampoConfiguracaoServico> {
	
	@Query("select campo " +
		   "  from CampoConfiguracaoServico campo inner join " +
		   "       campo.configuracaoServico servico " +
		   " where campo.campo = :campo and " +
		   "       servico.codigo = :codigo")
	CampoConfiguracaoServico getByCampoAndServico(@Param("campo") String campo, @Param("codigo") String servico);
	

}
