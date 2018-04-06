package br.com.totvs.cia.conciliacao.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.model.ConciliacaoForExecution;
import br.com.totvs.cia.conciliacao.model.ConciliacaoForPainel;

public interface ConciliacaoRepository extends JpaRepository<Conciliacao, String>, JpaSpecificationExecutor<Conciliacao> {

	@Query("select distinct " +
	   "           perfil.codigo as perfil, " +
	   "           perfil.layout.codigo as layout, " +
	   "           max(carga.id) as carga, " +
	   "           max(importacao.id) as importacao " +
	   "      from PerfilConciliacao perfil " +
	   "inner join perfil.configuracao config " +
	   " left join LoteCarga loteCarga with loteCarga.servico.id = config.servico.id " +
	   " left join loteCarga.carga carga with carga.data = ?1 " +
	   " left join Importacao importacao with importacao.layout.id = perfil.layout.id and importacao.dataImportacao = ?1 " +
	   "     where perfil.status = 'ATV' " +
	   "  group by perfil.codigo, perfil.layout.codigo")
	List<ConciliacaoForExecution> findByProjectedColumns(final Date data);
	
	@Query("select distinct " +
	   "           perfil.codigo as perfil, " +
	   "           perfil.layout.codigo as layout, " +
	   "           conciliacao.id as conciliacao, " +
	   "           max(carga.id) as carga, " +
	   "           max(importacao.id) as importacao " +
	   "      from PerfilConciliacao perfil " +
	   "inner join perfil.configuracao config " +
	   " left join Conciliacao conciliacao with conciliacao.data = ?1 and conciliacao.perfil.id = perfil.id " +
	   " left join LoteCarga loteCarga with loteCarga.servico.id = config.servico.id " +
	   " left join loteCarga.carga carga with carga.data = ?1 " +
	   " left join Importacao importacao with importacao.layout.id = perfil.layout.id and importacao.dataImportacao = ?1 " +
	   "     where perfil.status = 'ATV' " +
	   "  group by perfil.codigo, perfil.layout.codigo, conciliacao.id " +
	   "  order by perfil.codigo ")
	List<ConciliacaoForPainel> findProjectedColumnsForPainel(final Date data);
	
	@Query("select conciliacao " +
		   "  from Conciliacao conciliacao " +
		   " where conciliacao.data = ?1 and " +
		   "       conciliacao.perfil.codigo = ?2 ")
	Conciliacao findByDataAndCodigoPerfil (final Date data, final String perfil);
	
	@Query("select conciliacao " +
		   "  from Conciliacao conciliacao " +
		   " where conciliacao.data = ?1 and " +
		   "       conciliacao.perfil.id = ?2 ")
	Conciliacao findByDataAndIdPerfil (final Date data, final String perfil);

	@Query("select conciliacao " +
			   "from Conciliacao conciliacao " +
			   "where conciliacao.data = ?1 and " +
			   "   conciliacao.perfil.id = ?2")
	List<Conciliacao> findConciliacao(Date data, String perfilConciliacao);
	
	@Transactional
	@Modifying
	@Query("delete from Conciliacao conciliacao where conciliacao.id = :id")
	void deleteUnic(@Param("id")String id);
}