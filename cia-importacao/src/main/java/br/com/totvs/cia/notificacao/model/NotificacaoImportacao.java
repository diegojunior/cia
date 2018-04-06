package br.com.totvs.cia.notificacao.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.com.totvs.cia.importacao.model.Importacao;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NO_NOTIFICACAO_IMPORTACAO")
@PrimaryKeyJoinColumn(name = "ID_NOTIFICACAO")
@NoArgsConstructor
@Getter
public class NotificacaoImportacao extends AbstractNotificacao {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "ID_IMPORTACAO")
	private Importacao importacao;
	
	public NotificacaoImportacao(final Date data, final StatusEnum status, final String mensagem, final Importacao importacao) {
		super(data, status, mensagem);
		this.importacao = importacao;
	}
	
	public NotificacaoImportacao(final Date data, final StatusEnum status, final Importacao importacao) {
		super(data, status, status.toString());
		this.importacao = importacao;
	}
}
