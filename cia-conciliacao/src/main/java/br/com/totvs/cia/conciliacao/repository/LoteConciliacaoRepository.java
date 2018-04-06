package br.com.totvs.cia.conciliacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.conciliacao.model.LoteConciliacao;

public interface LoteConciliacaoRepository extends JpaRepository<LoteConciliacao, String>, JpaSpecificationExecutor<LoteConciliacao>{
	
	@Transactional
	@Modifying
	@Query("delete from LoteConciliacao lote where lote in (:lotes)")
	void deleteAll(@Param("lotes")List<LoteConciliacao> lotes);
	
	@Transactional
	@Modifying
	@Query("delete from LoteConciliacao lote where lote.id = :id")
	void deleteOne(@Param("id")String id);
	
}