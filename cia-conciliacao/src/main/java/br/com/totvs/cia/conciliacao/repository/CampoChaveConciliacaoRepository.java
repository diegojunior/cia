package br.com.totvs.cia.conciliacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.conciliacao.model.CampoChaveConciliacao;
import br.com.totvs.cia.conciliacao.model.UnidadeConciliacao;

public interface CampoChaveConciliacaoRepository extends JpaRepository<CampoChaveConciliacao, String>, JpaSpecificationExecutor<CampoChaveConciliacao>{

	@Transactional
	@Modifying
	@Query("delete from CampoChaveConciliacao campoChave where campoChave.unidade in (:unidades)")
	void deletePorUnidadeConciliacao(@Param("unidades")List<UnidadeConciliacao> unidadades);
	
}