package br.com.totvs.cia.notificacao.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.com.totvs.cia.carga.model.LoteCarga;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NO_NOTIFICACAO_CARGA")
@PrimaryKeyJoinColumn(name = "ID_NOTIFICACAO")
@NoArgsConstructor
@Getter
public class NotificacaoCarga extends AbstractNotificacao implements Comparable<NotificacaoCarga> {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "ID_LOTE_CARGA")
	private LoteCarga loteCarga;
	
	public NotificacaoCarga(final LoteCarga loteCarga, final StatusEnum statusInfra) {
		super(new Date(), statusInfra, loteCarga.getServico().getCodigo() + " - " + statusInfra.toString());
		this.loteCarga = loteCarga;
	}
	
	public NotificacaoCarga(final LoteCarga loteCarga, final StatusEnum statusInfra, final String mensagem) {
		super(new Date(), statusInfra, loteCarga.getServico().getCodigo() + " - " + statusInfra.toString() + mensagem);
		this.loteCarga = loteCarga;
	}
	
	public NotificacaoCarga(final LoteCarga loteCarga, final Date data, final StatusEnum statusInfra) {
		super(data, statusInfra, loteCarga.getServico().getCodigo() + " - " + statusInfra.toString());
		this.loteCarga = loteCarga;
	}

	public NotificacaoCarga(final LoteCarga loteCarga, final Date data, final StatusEnum statusInfra, final String mensagem) {
		super(data, statusInfra, mensagem);
		this.loteCarga = loteCarga;
	}

	@Override
	public int compareTo(NotificacaoCarga outra) {
		return this.getLoteCarga().getServico().getCodigo().compareTo(outra.getLoteCarga().getServico().getCodigo());
	}
}