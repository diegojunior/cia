package br.com.totvs.cia.importacao.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.FlowExecutionStatus;
import org.springframework.batch.core.job.flow.JobExecutionDecider;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.model.BlocoDelimitador;
import br.com.totvs.cia.importacao.model.LoteUnidadeImportacao;
import br.com.totvs.cia.importacao.model.UnidadeImportacaoSegregada;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;

@Configuration
@EnableBatchProcessing
public class ImportacaoBatchJob {

	private static final int CHUNK_LOTE = 300;
	
	private static final String OVERRIDDEN_BY_STRING_EXPRESSION = null;

	private static final String STEP_COMPLETED = "COMPLETED";

	private static final String TIPO_LAYOUT_XML = "XML";

	private static final String TIPO_LAYOUT_POSICIONAL = "POSICIONAL";
	
	private static final String TIPO_LAYOUT_DELIMITADOR = "DELIMITADOR";

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory steps;

	@Autowired
	private ImportacaoItemReader importacaoItemReader;

	@Autowired
	private ImportacaoItemXmlReader importacaoItemXmlReader;
	
	@Autowired
	private ImportacaoItemDelimitadorReader importacaoItemDelimitadorReader;

	@Autowired
	private ImportacaoItemLoteUnidadeReader importacaoItemLoteUnidadeReader;
	
	@Autowired
	private ImportacaoItemBlocoReader importacaoItemBlocoReader;

	@Autowired
	private ImportacaoItemProcessor importacaoItemProcessor;

	@Autowired
	private ImportacaoItemXmlProcessor importacaoItemXmlProcessor;
	
	@Autowired
	private ImportacaoItemDelimitadorProcessor importacaoItemDelimitadorProcessor;

	@Autowired
	private ImportacaoItemLoteUnidadeProcessor importacaoItemLoteUnidadeProcessor;
	
	@Autowired
	private ImportacaoItemBlocoProcessor importacaoItemBlocoProcessor;

	@Autowired
	private ImportacaoItemWriter importacaoItemWriter;

	@Autowired
	private ImportacaoItemXmlWriter importacaoItemXmlWriter;
	
	@Autowired
	private ImportacaoItemDelimitadorWriter importacaoItemDelimitadorWriter;

	@Autowired
	private ImportacaoItemLoteUnidadeWriter importacaoLoteUnidadeWriter;
	
	@Autowired
	private ImportacaoItemBlocoWriter importacaoItemBlocoUnidadeWriter;

	@Autowired
	@Qualifier(value = "notificacaoImportacaoListener")
	private NotificacaoImportacaoListener notificacaoStepListener;

	@Bean
	public Job importacaoJob() {
		return this.jobs
				.get("importacaoJob")
				.start(this.flow())
				.end()
				.listener(this.getNotificacaoImportacaoListener())
				.build();
	}

	@Bean
	protected Flow flow() {
		return new FlowBuilder<Flow>("flow")
				.start(this.deciderByImportType(OVERRIDDEN_BY_STRING_EXPRESSION))
					.on(TIPO_LAYOUT_POSICIONAL)
						.to(this.importacaoStep())
					.next(this.deciderByStatus())
						.on(STEP_COMPLETED)
							.to(this.importacaoLoteUnidadeStep())
				.from(this.deciderByImportType(OVERRIDDEN_BY_STRING_EXPRESSION))
					.on(TIPO_LAYOUT_XML)
						.to(this.importacaoXmlStep())
					.next(this.deciderByStatus())
						.on(STEP_COMPLETED)
							.to(this.importacaoLoteUnidadeStep())
				.from(this.deciderByImportType(OVERRIDDEN_BY_STRING_EXPRESSION))
					.on(TIPO_LAYOUT_DELIMITADOR)
						.to(this.importacaoDelimitadorStep())
					.next(this.deciderByStatusDelimitador())
						.on(STEP_COMPLETED)
							.to(this.importacaoBlocoUnidadeStep())
				.end();
	}

	@Bean
	@JobScope
	protected JobExecutionDecider deciderByImportType(@Value("#{jobParameters[layoutType]}") final String layoutTypeParam) {

		return new JobExecutionDecider() {

			private TipoLayoutEnum tipoLayout;

			@Override
			public FlowExecutionStatus decide(final JobExecution jobExecution, final StepExecution stepExecution) {
				if (this.tipoLayout == null) {
					this.tipoLayout = TipoLayoutEnum.valueOf(layoutTypeParam);
				}

				return new FlowExecutionStatus(this.tipoLayout.name());
			}

		};
	}
	
	@Bean
	@JobScope
	protected JobExecutionDecider deciderByStatus() {
		return new JobExecutionDecider() {
			
			@Override
			public FlowExecutionStatus decide(final JobExecution jobExecution, final StepExecution stepExecution) {
				return new FlowExecutionStatus(stepExecution.getStatus().name());
			}
		};
	}
	
	@Bean
	@JobScope
	protected JobExecutionDecider deciderByStatusDelimitador() {
		return new JobExecutionDecider() {
			
			@Override
			public FlowExecutionStatus decide(final JobExecution jobExecution, final StepExecution stepExecution) {
				return new FlowExecutionStatus(stepExecution.getStatus().name());
			}
		};
	}

	@Bean
	protected Step importacaoXmlStep() {
		return this.steps.get("importacaoXmlStep").<UnidadeImportacaoProcessamentoJson, UnidadeLayoutImportacao>chunk(CHUNK_LOTE)
				.reader(this.importacaoXmlReader(OVERRIDDEN_BY_STRING_EXPRESSION))
				.processor(this.importacaoXmlProcessor(OVERRIDDEN_BY_STRING_EXPRESSION))
				.writer(this.importacaoXmlWriter())
				.build();
	}

	@Bean
	protected Step importacaoStep() {
		return this.steps.get("importacaoStep").<UnidadeImportacaoProcessamentoJson, UnidadeLayoutImportacao>chunk(CHUNK_LOTE)
				.reader(this.importacaoReader(OVERRIDDEN_BY_STRING_EXPRESSION))
				.processor(this.importacaoProcessor(OVERRIDDEN_BY_STRING_EXPRESSION))
				.writer(this.importacaoWriter())
				.build();
	}
	
	@Bean
	protected Step importacaoDelimitadorStep() {
		return this.steps.get("delimitadorStep")
				.<UnidadeImportacaoProcessamentoJson, UnidadeLayoutImportacao>chunk(CHUNK_LOTE)
				.reader(this.importacaoDelimitadorReader(OVERRIDDEN_BY_STRING_EXPRESSION))
				.processor(this.importacaoDelimitadorProcessor(OVERRIDDEN_BY_STRING_EXPRESSION))
				.writer(this.importacaoDelimitadorWriter())
				.build();
	}

	@Bean
	protected Step importacaoLoteUnidadeStep() {
		return this.steps.get("loteUnidadeStep").<LoteUnidadeImportacao, UnidadeImportacaoSegregada>chunk(CHUNK_LOTE)
				.reader(this.importacaoLoteUnidadeReader(OVERRIDDEN_BY_STRING_EXPRESSION))
				.processor(this.importacaoLoteUnidadeProcessor(OVERRIDDEN_BY_STRING_EXPRESSION))
				.writer(this.importacaoLoteUnidadeWriter())
				.build();
	}
	
	@Bean
	protected Step importacaoBlocoUnidadeStep() {
		return this.steps.get("blocoUnidadeStep").<BlocoDelimitador, UnidadeImportacaoSegregada>chunk(CHUNK_LOTE)
				.reader(this.importacaoBlocoReader(OVERRIDDEN_BY_STRING_EXPRESSION))
				.processor(this.importacaoBlocoUnidadeProcessor(OVERRIDDEN_BY_STRING_EXPRESSION))
				.writer(this.importacaoBlocoUnidadeWriter())
				.build();
	}

	@Bean
	@StepScope
	protected ItemReader<UnidadeImportacaoProcessamentoJson> importacaoXmlReader(
			@Value("#{jobParameters[importacao]}") final String importacaoId) {

		return this.importacaoItemXmlReader.reader(importacaoId);

	}

	@Bean
	@StepScope
	protected ItemReader<? extends UnidadeImportacaoProcessamentoJson> importacaoReader(
			@Value("#{jobParameters[importacao]}") final String importacaoId) {
		return this.importacaoItemReader.reader(importacaoId);
	}
	
	@Bean
	@StepScope
	protected ItemReader<? extends UnidadeImportacaoProcessamentoJson> importacaoDelimitadorReader(@Value("#{jobParameters[importacao]}")final String importacaoId) {
		return this.importacaoItemDelimitadorReader.loadReader(importacaoId);
	}

	@Bean
	@StepScope
	protected ItemReader<LoteUnidadeImportacao> importacaoLoteUnidadeReader(
			@Value("#{jobParameters[importacao]}") final String importacaoId) {
		return this.importacaoItemLoteUnidadeReader.reader(importacaoId, CHUNK_LOTE);

	}
	
	@Bean
	@StepScope
	protected ItemReader<BlocoDelimitador> importacaoBlocoReader(@Value("#{jobParameters[importacao]}") final String importacaoId) {
		return this.importacaoItemBlocoReader.reader(importacaoId);
	}

	@Bean
	@StepScope
	protected ItemProcessor<? super UnidadeImportacaoProcessamentoJson, ? extends UnidadeLayoutImportacao> importacaoProcessor(
			@Value("#{jobParameters[importacao]}") final String importacaoId) {
		this.importacaoItemProcessor.carregaDominios();
		this.importacaoItemProcessor.carregaValidacoesTransformacoes(importacaoId);
		return this.importacaoItemProcessor;
	}

	@Bean
	@StepScope
	protected ItemProcessor<UnidadeImportacaoProcessamentoJson, ? extends UnidadeLayoutImportacao> importacaoXmlProcessor(
			@Value("#{jobParameters[importacao]}") final String importacaoId) {
		this.importacaoItemXmlProcessor.carregaDominios();
		this.importacaoItemXmlProcessor.carregaValidacoesTransformacoes(importacaoId);
		return this.importacaoItemXmlProcessor;
	}
	
	@Bean
	@StepScope
	protected ItemProcessor<? super UnidadeImportacaoProcessamentoJson, ? extends UnidadeLayoutImportacao>  importacaoDelimitadorProcessor(@Value("#{jobParameters[importacao]}")final String importacaoId) {
		this.importacaoItemDelimitadorProcessor.loadData(importacaoId);
		return this.importacaoItemDelimitadorProcessor;
	}

	@Bean
	@StepScope
	protected ItemProcessor<? super LoteUnidadeImportacao, ? extends UnidadeImportacaoSegregada> importacaoLoteUnidadeProcessor(
			@Value("#{jobParameters[importacao]}")final String importacaoId) {
		return this.importacaoItemLoteUnidadeProcessor.processor(importacaoId);
	}
	
	@Bean
	@StepScope
	protected ItemProcessor<? super BlocoDelimitador, ? extends UnidadeImportacaoSegregada> importacaoBlocoUnidadeProcessor(
			@Value("#{jobParameters[importacao]}")final String importacaoId) {
		return this.importacaoItemBlocoProcessor.processor(importacaoId);
	}

	@Bean
	@StepScope
	protected ItemWriter<? super UnidadeLayoutImportacao> importacaoXmlWriter() {
		return this.importacaoItemXmlWriter;
	}

	@Bean
	@StepScope
	protected ItemWriter<? super UnidadeLayoutImportacao> importacaoWriter() {
		return this.importacaoItemWriter;
	}
	
	@Bean
	@StepScope
	protected ItemWriter<? super UnidadeLayoutImportacao> importacaoDelimitadorWriter() {
		return this.importacaoItemDelimitadorWriter;
	}
	
	@Bean
	@StepScope
	protected ItemWriter<? super UnidadeImportacaoSegregada> importacaoLoteUnidadeWriter() {
		return this.importacaoLoteUnidadeWriter;
	}
	
	@Bean
	@StepScope
	protected ItemWriter<? super UnidadeImportacaoSegregada> importacaoBlocoUnidadeWriter() {
		return this.importacaoItemBlocoUnidadeWriter;
	}

	@Bean
	@JobScope
	protected NotificacaoImportacaoListener getNotificacaoImportacaoListener() {
		return this.notificacaoStepListener;
	}
}
