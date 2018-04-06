
package br.com.totvs.cia.carga.job;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.UnidadeCarga;
import br.com.totvs.cia.carga.service.CargaService;
import br.com.totvs.cia.carga.service.LoteCargaService;
import br.com.totvs.cia.carga.service.LoteClienteService;
import br.com.totvs.cia.carga.service.UnidadeCargaService;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.service.ProcessamentoGatewayService;
import br.com.totvs.cia.notificacao.service.NotificacaoCargaService;
import lombok.NoArgsConstructor;

@Configuration
@NoArgsConstructor
@EnableBatchProcessing
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CargaJob {
	
	private static final String FLOW = "_FLOW";

	private static final String STEP = "_STEP";

	@Autowired
    private JobBuilderFactory jobs;
	
	@Autowired
    private StepBuilderFactory steps;
	
	@Autowired
	private CargaService cargaService;
	
	@Autowired
	private LoteCargaService loteCargaService;
	
	@Autowired
	private LoteClienteService loteClienteService;
	
	@Autowired
	private UnidadeCargaService unidadeCargaService;
	
	@Autowired
	private ProcessamentoGatewayService processamentoGatewayService;
	
	@Autowired
	private NotificacaoCargaService notificacaoCargaService;
	
	@Autowired @Qualifier(value="cargaJobListener")
	private CargaJobListener jobListener;
	
	@Bean
    public Job cargaBatchJob() {
		Flow[] flows = this.getParallelsFlows();
		
		return this.jobs.get("cargaBatchJob")
        		   .incrementer(new RunIdIncrementer())
        		   .listener(this.getCargaJobListener())
        		   .start(this.getCargaLogStep())
        		   .split(new SimpleAsyncTaskExecutor()).add(flows)
        		   .end()
        		   .build();
    }

	private Flow[] getParallelsFlows() {
    		List<Flow> flows = new ArrayList<Flow>();
    		for (ServicoEnum servico : ServicoEnum.values()) {
    			String stepName = servico.getNome() + STEP;
    			String flowName = servico.getNome() + FLOW;
    			Flow flow = new FlowBuilder<SimpleFlow>(flowName).start(this.processamentoCargaStep(stepName, servico)).build();
    			flows.add(flow);
    		}
    		
    		return flows.stream().toArray(size -> new Flow[size]);
	}
    
    private Step getCargaLogStep() {
    		return this.steps.get("cargaLogStep")
    			 	 .<Carga, Carga>chunk(1)
    			 	 .reader(this.getCargaLogItemReader())
    			 	 .writer(this.getCargaLogItemWriter())
    			 	 .build();
    }
    
	private CargaLogItemReader getCargaLogItemReader() {
    		return new CargaLogItemReader(this.cargaService);
    	}
    
	private CargaLogItemWriter getCargaLogItemWriter(){
    		return new CargaLogItemWriter();
	}

    private Step processamentoCargaStep(final String stepName, final ServicoEnum servico) {
    		return this.steps.get(stepName)
    			 	 .<UnidadeProcessamentoJson, UnidadeCarga>chunk(1000)
    			 	 .reader(this.getCargaItemReader(servico))
    			 	 .processor(this.getCargaItemProcessor(servico))
    			 	 .writer(this.getCargaItemWriter())
    			 	 .listener(this.getCargaStepListener(servico))
    			 	 .build();
    }
    
	private CargaItemReader getCargaItemReader(final ServicoEnum servico){
	    CargaItemReader cargaItemReader = new CargaItemReader(this.cargaService, this.loteCargaService, this.loteClienteService, this.processamentoGatewayService);
	    cargaItemReader.setServico(servico);
	    return cargaItemReader;
	}
    
	private ItemProcessor<UnidadeProcessamentoJson, UnidadeCarga> getCargaItemProcessor(final ServicoEnum servico){
		CargaItemProcessor cargaItemProcessor = new CargaItemProcessor(this.cargaService);
		cargaItemProcessor.setServico(servico);
		return cargaItemProcessor;
	}
    
	private CargaItemWriter getCargaItemWriter(){
    	return new CargaItemWriter(this.loteClienteService, this.unidadeCargaService);
	}
	
	private CargaStepListener getCargaStepListener(final ServicoEnum servico){
    	CargaStepListener cargaStepListener = new CargaStepListener(this.loteCargaService, this.notificacaoCargaService);
    	cargaStepListener.setServico(servico);
    	return cargaStepListener;
	}
    
    private CargaJobListener getCargaJobListener(){
    	return this.jobListener;
    }
}