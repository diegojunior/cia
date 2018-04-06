package br.com.totvs.cia.carga.job;

import java.util.Arrays;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.carga.service.LoteCargaService;
import br.com.totvs.cia.notificacao.service.NotificacaoCargaService;
import lombok.Setter;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CargaStepListener implements StepExecutionListener {

	private LoteCargaService loteCargaService;

	private NotificacaoCargaService notificacaoCargaService;
	
	@Setter
	private ServicoEnum servico;
	
	private Boolean isExecutable;
	
	@Autowired
	public CargaStepListener (final LoteCargaService loteCargaService, final NotificacaoCargaService notificacaoCargaService) {
		this.loteCargaService = loteCargaService;
		this.notificacaoCargaService = notificacaoCargaService;
	}
	
	@Override
	public void beforeStep(final StepExecution stepExecution) {
		LoteCarga lote = this.loteCargaService.findByCargaAndServico(stepExecution.getJobParameters().getString("carga"), this.servico);
		this.isExecutable = lote != null && lote.getIsExecucaoAtivada();
		if (this.isExecutable) {
			this.removeNotificacoesExistentes(stepExecution);
			this.notificaInicio(stepExecution);
		}
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		if (this.isExecutable) {
			return this.concluir(stepExecution);
		}
		return ExitStatus.STOPPED;
	}
		
	private ExitStatus concluir(final StepExecution stepExecution) {
		final Boolean isHouveErro = this.isGerouException(stepExecution);
		if (isHouveErro) {
			this.loteCargaService.concluirComErro(stepExecution.getJobParameters().getString("carga"), this.servico);
			this.notificaErroInterno(stepExecution);
			return ExitStatus.FAILED;
		}
		this.loteCargaService.concluirComSucesso(stepExecution.getJobParameters().getString("carga"), this.servico);
		this.notificaConclusao(stepExecution);
		return ExitStatus.COMPLETED;			
	}
		
	private void removeNotificacoesExistentes(final StepExecution stepExecution) {
		this.notificacaoCargaService.removeBy(stepExecution.getJobParameters().getString("carga"), this.servico);
	}
	
	private void notificaInicio(final StepExecution stepExecution) {
		this.notificacaoCargaService.notificaInicio(stepExecution.getJobParameters().getString("carga"), this.servico);
	}
	
	private void notificaErroInterno(final StepExecution stepExecution) {
		StringBuilder erro = new StringBuilder();
		for (final Throwable error : stepExecution.getFailureExceptions()) {
			erro.append(Arrays.toString(error.getStackTrace())).append("<br />");
		}
		this.notificacaoCargaService.notificaErroInterno(stepExecution.getJobParameters().getString("carga"), this.servico, erro.toString());
	}
	
	private void notificaConclusao(final StepExecution stepExecution) {
		this.notificacaoCargaService.notificaConclusao(stepExecution.getJobParameters().getString("carga"), this.servico);
	}
	
	private Boolean isGerouException(final StepExecution stepExecution) {
		if(stepExecution.getStatus().isUnsuccessful()){
			for (final Throwable error : stepExecution.getFailureExceptions()) {
				if (error instanceof RuntimeException) {
					return true;
				}
			}
		}
		return false;
	}
}