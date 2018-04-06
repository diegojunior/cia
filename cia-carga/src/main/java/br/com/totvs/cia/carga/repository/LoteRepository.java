package br.com.totvs.cia.carga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.carga.model.LoteCarga;

public interface LoteRepository extends JpaRepository<LoteCarga, String>, JpaSpecificationExecutor<LoteCarga>{
	
	@Query(value = "select lote " +
		           "  from LoteCarga lote left join fetch lote.lotesClientes lotes " +
		           " where lote.id = :id")  
	LoteCarga findByIdFetchLotes(@Param("id") final String id);
	
	@Query(value = "    select lote " +
		           "      from LoteCarga lote " +
		           "      join lote.carga carga " +
		           "      join lote.servico configuracaoServico " +
		           "     where carga.id = :carga " +
		           "       and configuracaoServico.servico = :servico ")
	LoteCarga findByCargaAndServico(@Param("carga") final String carga, @Param("servico") final ServicoEnum servico);
	
	@Query(value = "         select lote " +
		           "           from LoteCarga lote " +
		           "           join lote.carga carga " +
		           "           join lote.servico configuracaoServico " +
		           "left join fetch lote.lotesClientes lotesClientes " +
		           "     where carga.id = :carga " +
		           "       and configuracaoServico.servico = :servico ")
	LoteCarga findByCargaAndServicoFetchLotes(@Param("carga") final String carga, @Param("servico") final ServicoEnum servico);
}