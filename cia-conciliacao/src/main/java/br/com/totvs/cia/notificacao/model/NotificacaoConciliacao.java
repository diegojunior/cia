package br.com.totvs.cia.notificacao.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.infra.util.DateUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NA_NOTIFICACAO_CONCILIACAO")
@PrimaryKeyJoinColumn(name = "ID_NOTIFICACAO")
@NoArgsConstructor
@Getter
public class NotificacaoConciliacao extends AbstractNotificacao implements NotificacaoConciliacaoState {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "ID_CONCILIACAO")
	private Conciliacao conciliacao;
	
	private NotificacaoConciliacao(final Date data, final StatusEnum status, final String mensagem) {
		super(data, status, mensagem);
	}
	
	public static NotificacaoConciliacao iniciar(final Date data, final Conciliacao conciliacao, final int totalCarga, final int totalImportacao) {
		StringBuilder msg = gerarMsg(conciliacao, data);
		msg.append("Linhas a importar: ").append(totalImportacao).append("<br />")
		.append("Linhas a carregar: ").append(totalCarga).append("<br />");
		final NotificacaoConciliacao notificacao = new NotificacaoConciliacao(data, StatusEnum.INICIADO, msg.toString());
		notificacao.doExecute(conciliacao);
		return notificacao;
	}
	
	public static NotificacaoConciliacao concluirNotificacao(final Date data, final Conciliacao conciliacao) {
		StringBuilder msg = gerarMsg(conciliacao, data);
		msg.append("Unidades de Conciliação:").append("<br />")
		.append("OK: ").append(conciliacao.getLote().quantidadeUnidadeOk()).append("<br />")
		.append("Divergentes: ").append(conciliacao.getLote().quantidadeUnidadeDivergente()).append("<br />")
		.append("Chave não encontrada: ").append(conciliacao.getLote().quantidadeUnidadeChaveNaoIdentificada()).append("<br />");
		final NotificacaoConciliacao notificacao = new NotificacaoConciliacao(data, StatusEnum.CONCLUIDO, msg.toString());
		notificacao.doExecute(conciliacao);
		return notificacao;
	}
	
	public static NotificacaoConciliacao notificarErro(final Conciliacao conciliacao, final String msg) {
		final NotificacaoConciliacao notificacao = new NotificacaoConciliacao(new Date(), StatusEnum.ERRO, msg);
		notificacao.doExecute(conciliacao);
		return notificacao;
	}
	

	@Override
	public void doExecute(final Conciliacao conciliacao) {
		this.conciliacao = conciliacao;
	}
	
	public static StringBuilder gerarMsg(final Conciliacao conciliacao, final Date data) {
		final StringBuilder sb = new StringBuilder();
		String dataFormatada = DateUtil.format(data, DateUtil.ddMMyyyyHHmmss);
		sb.append("Data Conciliação: ").append(DateUtil.format(conciliacao.getData(), DateUtil.ddMMyyyyHHmmss))
		.append("<br />")
		.append("Sistema: ").append(conciliacao.getPerfil().getSistema().getNome()).append("<br />")
		.append("Layout: ").append(conciliacao.getPerfil().getLayout().getCodigo()).append("<br />")
		.append("Perfil: ").append(conciliacao.getPerfil().getCodigo()).append("<br />")
		.append("Data Hora: ").append(dataFormatada).append("<br />");
		
		return sb;
	}
}