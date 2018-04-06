package br.com.totvs.cia.importacao.job;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.importacao.exception.ImportacaoArquivoParseException;
import br.com.totvs.cia.importacao.exception.ValidacaoInternaException;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.StatusImportacaoEnum;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.importacao.service.UnidadeImportacaoService;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.notificacao.model.NotificacaoImportacao;
import br.com.totvs.cia.notificacao.model.StatusEnum;
import br.com.totvs.cia.notificacao.service.NotificacaoImportacaoService;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoService;
import br.com.totvs.cia.parametrizacao.validacao.model.AbstractValidacaoArquivo;
import br.com.totvs.cia.parametrizacao.validacao.service.ValidacaoArquivoService;

@Component
public class NotificacaoImportacaoListener implements JobExecutionListener {
	
	private static final Logger LOG = Logger.getLogger(NotificacaoImportacaoListener.class);
	
	@Autowired
	private ImportacaoService importacaoService;
	
	@Autowired
	private NotificacaoImportacaoService notificacaoImportacaoService;
	
	@Autowired
	private UnidadeImportacaoService unidadeImportacaoService;
	
	@Autowired
	private TransformacaoService transformacaoService;
	
	@Autowired
	private ValidacaoArquivoService validacaoService;

	@BeforeJob
	@Override
	public void beforeJob(final JobExecution jobExecution) {
		LOG.debug("INICIO - Interceptado pelo Listener de notificação da importação.");
		final String importacaoId = jobExecution.getJobParameters().getString("importacao");
		final Importacao importacao = this.importacaoService.findOne(importacaoId);
		final Date data = new Date();
		final StringBuilder msg = this.gerarMensagemNotificacao(importacao, data);
		final NotificacaoImportacao notificacaoImportacao = new NotificacaoImportacao(data, StatusEnum.INICIADO, msg.toString(), importacao);
		this.notificacaoImportacaoService.save(notificacaoImportacao);
	}

	@AfterJob
	@Override
	public void afterJob(final JobExecution jobExecution) {
		LOG.debug("FIM - Interceptado pelo Listener de notificação da importação.");
		final NotificacaoImportacao notificacaoImportacao = this.concluirJobImportacao(jobExecution);
		this.notificacaoImportacaoService.save(notificacaoImportacao);
		
	}

	private NotificacaoImportacao concluirJobImportacao(final JobExecution jobExecution) {
		final Importacao importacao = this.importacaoService.findOne(jobExecution.getJobParameters().getString("importacao"));
		final Date data = new Date();
		StringBuilder builderMsg = this.gerarMensagemNotificacao(importacao, data);
		if (this.houveErro(jobExecution)) {
			builderMsg.append(this.tratarErro(jobExecution));
			importacao.setStatus(StatusImportacaoEnum.ERRO);
			this.importacaoService.save(importacao);
			return new NotificacaoImportacao(data, StatusEnum.ERRO, builderMsg.toString(), importacao);
		}
		
		long qtdUnidades = this.unidadeImportacaoService.countUnidadesImportadasPor(importacao);
		builderMsg.append("Importados: ").append(qtdUnidades).append("<br />");
		this.notificarTransformacao(importacao, builderMsg);
		this.notificarValidacao(importacao, builderMsg);
		this.notificaRemetente(importacao, builderMsg);
		importacao.setStatus(StatusImportacaoEnum.CONCLUIDO);
		this.importacaoService.save(importacao);
		return new NotificacaoImportacao(data, StatusEnum.CONCLUIDO, builderMsg.toString(), importacao);
	}

	private boolean houveErro(final JobExecution jobExecution) {
		return jobExecution.getStatus().isUnsuccessful();
	}

	private StringBuilder tratarErro(final JobExecution jobExecution) {
		StringBuilder sb = new StringBuilder();
		for (final Throwable error : jobExecution.getAllFailureExceptions()) {
			LOG.error(error.getMessage(), error);
			if (error instanceof ValidacaoInternaException) {
				sb.append(error.getMessage());
			} else if (error instanceof ImportacaoArquivoParseException){
				sb.append("O arquivo importado possui divergência com o Layout. ")
				.append(error.getMessage());
			} else if (error instanceof RuntimeException) {
				sb.append("Ocorreu um erro interno ao executar a importação. Contate Administrador do Sistema. ")
				.append(error.getMessage());
			}
		}
		return sb;
		
	}
	
	private StringBuilder gerarMensagemNotificacao(final Importacao importacao, final Date dataHora) {
		StringBuilder sb = new StringBuilder();
		sb.append("Data Arquivo: ").append(DateUtil.format(importacao.getDataImportacao(), DateUtil.ddMMyyyyHHmmss)).append("<br />")
		.append("Sistema: ").append(importacao.getSistema() != null ? importacao.getSistema().getNome() : "").append("<br />")
		.append("Layout: ").append(importacao.getLayout().getCodigo()).append("<br />")
		.append("Data Hora: ").append(DateUtil.format(dataHora, DateUtil.ddMMyyyyHHmmss)).append("<br />");
		
		return sb;
	}
	
	private void notificaRemetente(final Importacao importacao, final StringBuilder builderMsg) {
		if (importacao.getRemetente() != null) {
			builderMsg.append("Remetente Utilizado: ").append(importacao.getRemetente().getCodigo()).append("<br />");
		}
	}

	private void notificarValidacao(final Importacao importacao, final StringBuilder builderMsg) {
		List<AbstractValidacaoArquivo> validacoes = this.validacaoService.listBy(importacao.getLayout().getTipoLayout(), importacao.getLayout());
		if (!validacoes.isEmpty()) {
			builderMsg.append("Validação realizada.").append("<br />");
		}
	}

	private void notificarTransformacao(final Importacao importacao, final StringBuilder builderMsg) {
		List<Transformacao> transformacoes = this.transformacaoService.getBy(importacao.getLayout().getTipoLayout(), importacao.getLayout().getCodigo());
		if (!transformacoes.isEmpty()) {
			builderMsg.append("Transformação realizada.").append("<br />");
		}
	}
	
}