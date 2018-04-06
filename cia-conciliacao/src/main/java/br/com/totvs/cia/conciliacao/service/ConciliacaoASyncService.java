package br.com.totvs.cia.conciliacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.conciliacao.converter.ConciliacaoExecucaoConverter;
import br.com.totvs.cia.conciliacao.json.ConciliacaoExecucaoJson;
import br.com.totvs.cia.conciliacao.model.Conciliacao;

@Service
public class ConciliacaoASyncService {

	@Autowired
	private ProcessamentoConciliacao processamentoConciliacao;
	
	@Autowired
	private ConciliacaoExecucaoConverter conciliacaoExecucaoConverter;
	
	@Async
	@Transactional
	public void executar(final ConciliacaoExecucaoJson json) {
		final Conciliacao conciliacao = this.conciliacaoExecucaoConverter.convertFrom(json);
		this.processamentoConciliacao.processar(conciliacao);
	}
	
}