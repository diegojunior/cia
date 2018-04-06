package br.com.totvs.cia.carga.job;

import java.util.Collections;
import java.util.Iterator;

import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.service.CargaService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CargaLogItemReader implements ItemReader<Carga>{
	
	private CargaService cargaService;
	
	private Iterator<Carga> itensReader;
	
	@Autowired
	public CargaLogItemReader (final CargaService cargaService) {
		this.cargaService = cargaService;
	}
	
	@BeforeStep
	public void beforeStep (final StepExecution stepExecution) {
		String carga = stepExecution.getJobExecution().getJobParameters().getString("carga");
		this.itensReader = Collections.singleton(this.cargaService.getCargaFetchLotesBy(carga)).iterator();
	}
	
	@Override
    public Carga read() {
		return this.itensReader != null && this.itensReader.hasNext()? this.itensReader.next() : null;
    }
}