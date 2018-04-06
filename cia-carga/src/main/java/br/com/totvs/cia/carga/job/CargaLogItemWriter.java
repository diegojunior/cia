package br.com.totvs.cia.carga.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.carga.model.Carga;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CargaLogItemWriter implements ItemWriter<Carga>{
	
	private static final Logger log = Logger.getLogger(CargaLogItemWriter.class);
	
	private String tipoExecucao;
		
	@BeforeStep
	public void beforeStep (final StepExecution stepExecution) {
		this.tipoExecucao = stepExecution.getJobExecution().getJobParameters().getString("tipoExecucao");
	}
	
	@Override
	public void write(final List<? extends Carga> items) throws Exception {
		Carga carga = items.stream().findFirst().get();
		log.info("Carga preparada para execução por "+tipoExecucao+" dos seguintes serviços:" + carga.getId());
	}
}