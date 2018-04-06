package br.com.totvs.cia.importacao.job;

import org.springframework.batch.core.job.flow.FlowExecutionStatus;

import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;

public class LayoutFlowExecutionStatus extends FlowExecutionStatus {
	
	public LayoutFlowExecutionStatus(String status) {
		super(status);
	}

}
