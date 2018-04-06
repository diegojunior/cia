package br.com.totvs.cia.notificacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.notificacao.model.NotificacaoImportacao;

public interface NotificacaoImportacaoRepository extends JpaRepository<NotificacaoImportacao, String>, JpaSpecificationExecutor<NotificacaoImportacao> {

}
