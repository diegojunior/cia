package br.com.totvs.cia.carga.job;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.carga.model.LoteCliente;
import br.com.totvs.cia.carga.model.StatusLoteClienteEnum;
import br.com.totvs.cia.carga.model.UnidadeCarga;
import br.com.totvs.cia.carga.service.CargaService;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import lombok.Setter;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CargaItemProcessor implements ItemProcessor<UnidadeProcessamentoJson, UnidadeCarga> {
	
	private static final Logger log = Logger.getLogger(CargaItemProcessor.class);
	
	private CargaService cargaService;
	
	private LoteCarga loteCarga;
	
	@Setter
	private ServicoEnum servico;
	
	@Autowired
	public CargaItemProcessor (final CargaService cargaService) {
		this.cargaService = cargaService;
	}
	
	@BeforeStep
	public void beforeStep (final StepExecution stepExecution) {
		String idCarga = stepExecution.getJobExecution().getJobParameters().getString("carga");
		this.loteCarga = this.cargaService.getCargaFetchLotesBy(idCarga).getLote(this.servico);
	}
	
	@Override
	public UnidadeCarga process(final UnidadeProcessamentoJson json) throws Exception {
		try {
			if (this.loteCarga != null && this.loteCarga.getIsExecucaoAtivada()) {
				LoteCliente loteCliente = new LoteCliente(this.loteCarga, json.getCliente(), StatusLoteClienteEnum.SUCESSO);
	
				return new UnidadeCarga (loteCliente, json.getCampos());
			}
		} catch (Exception ex) {
			log.error("Não foi possível criar a Unidade de Carga.", ex);
		}
		return null;
	}
}