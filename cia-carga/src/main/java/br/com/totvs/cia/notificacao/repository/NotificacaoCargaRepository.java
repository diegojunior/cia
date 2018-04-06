package br.com.totvs.cia.notificacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.notificacao.model.NotificacaoCarga;
import br.com.totvs.cia.notificacao.model.StatusEnum;

public interface NotificacaoCargaRepository extends JpaRepository<NotificacaoCarga, String>, JpaSpecificationExecutor<NotificacaoCarga> {
	
	List<NotificacaoCarga> findByLoteCargaAndStatus(final LoteCarga lote, final StatusEnum status);
	
	@Query(value = "select notificacao " +
			       "  from NotificacaoCarga notificacao join " +
			       "	   	   notificacao.loteCarga lote join " +
			       "       lote.carga carga " +
			       " where carga in (:cargas)") 
	List<NotificacaoCarga> findByCargas(@Param("cargas") final List<Carga> cargas);
	
	@Query(value = "select notificacao " +
			       "  from NotificacaoCarga notificacao " +
			       "  join notificacao.loteCarga lote " +
			       "  join lote.servico configuracaoServico " +
			       "  join lote.carga carga " +
			       " where carga.id = :carga " +
			       "   and configuracaoServico.servico = :servico") 
	List<NotificacaoCarga> findByCargaAndServico(@Param("carga") final String carga, @Param("servico") final ServicoEnum servico);

}
