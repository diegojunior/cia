package br.com.totvs.cia.conciliacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.conciliacao.model.LoteConciliacao;
import br.com.totvs.cia.conciliacao.model.UnidadeConciliacao;

public interface UnidadeConciliacaoRepository extends JpaRepository<UnidadeConciliacao, String>, JpaSpecificationExecutor<UnidadeConciliacao>{
	
	@Transactional
	@Modifying
	@Query("delete from UnidadeConciliacao unidade where unidade.lote in (:lote)")
	void deletePorLoteConciliacao(@Param("lote") LoteConciliacao lote);
}