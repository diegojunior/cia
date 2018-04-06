package br.com.totvs.cia.notificacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.notificacao.model.NotificacaoConciliacao;

public interface NotificacaoConciliacaoRepository extends JpaRepository<NotificacaoConciliacao, String>, JpaSpecificationExecutor<NotificacaoConciliacao> {

	@Transactional
	@Modifying
	@Query("delete from NotificacaoConciliacao notificacao where notificacao.conciliacao in (:conciliacao)")
	void deleteNotificacoesByConciliacao(@Param("conciliacao") Conciliacao conciliacao);
	
}
