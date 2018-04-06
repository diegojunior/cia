package br.com.totvs.cia.carga.job;

import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.carga.model.LoteCliente;
import br.com.totvs.cia.carga.service.CargaService;
import br.com.totvs.cia.carga.service.LoteCargaService;
import br.com.totvs.cia.carga.service.LoteClienteService;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.service.ProcessamentoGatewayService;
import br.com.totvs.cia.integracao.core.exception.GatewayException;
import lombok.Setter;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CargaItemReader implements ItemReader<UnidadeProcessamentoJson>{
	
	private static final Logger log = Logger.getLogger(CargaItemReader.class);
	
	private CargaService cargaService;
	
	private LoteCargaService loteCargaService;
	
	private LoteClienteService loteClienteService; 
	
	private ProcessamentoGatewayService processamentoGatewayService;
	
	@Setter
	private ServicoEnum servico;
	
	private Iterator<UnidadeProcessamentoJson> itensReader;
	
	@Autowired
	public CargaItemReader(final CargaService cargaService, final LoteCargaService loteCargaService,
			final LoteClienteService loteClienteService, final ProcessamentoGatewayService processamentoGatewayService) {
		this.cargaService = cargaService;
		this.loteCargaService = loteCargaService;
		this.loteClienteService = loteClienteService;
		this.processamentoGatewayService = processamentoGatewayService;
	}
	
	@BeforeStep
	public void beforeStep (final StepExecution stepExecution) {
		String idCarga = stepExecution.getJobExecution().getJobParameters().getString("carga");
		String tipoExecucao = stepExecution.getJobExecution().getJobParameters().getString("tipoExecucao");
		
		LoteCarga lote = this.getFullLoteCargaByIdCarga(idCarga);
		
		if (lote != null) {
			if (tipoExecucao.equals("CL")) {
				this.consomeSistemaOrigemPorCliente(lote);
			} else if (tipoExecucao.equals("TO")) {
				this.consomeSistemaOrigemPorData(lote);
			}
		}
	}
	
	@Override
    public UnidadeProcessamentoJson read() {
		return this.itensReader != null && this.itensReader.hasNext()? this.itensReader.next() : null;
    }

	private void consomeSistemaOrigemPorCliente(final LoteCarga lote) {
		final List<UnidadeProcessamentoJson> unidades = Lists.newArrayList();
		for (LoteCliente loteCliente : lote.getLotesClientesComExecucaoAtivada()) {
			try {
				List<UnidadeProcessamentoJson> unidadesObtidas = this.processamentoGatewayService.listBy(lote.getCarga().getData(),
						lote.getServico(), loteCliente.getCliente());
				
				if (unidadesObtidas == null || unidadesObtidas.isEmpty()) {
					this.loteClienteService.atualizaStatusComWarning(loteCliente);
				}
	
				unidades.addAll(unidadesObtidas);
			} catch (final GatewayException ie) {
				log.error("Nao foi possivel consumir os dados: " + ie.getMessage(), ie);
				this.loteClienteService.atualizaStatusComErro(loteCliente);
			}
		}
		this.itensReader =  unidades.iterator();
	}
	
	private void consomeSistemaOrigemPorData(final LoteCarga lote) {
		try {
			final List<UnidadeProcessamentoJson> unidades = this.processamentoGatewayService.listBy(
					lote.getCarga().getData(), lote.getServico());

			this.itensReader = unidades.iterator();
		} catch (final GatewayException ie) {
			log.error("Nao foi possivel consumir os dados: " + ie.getMessage(), ie);
			throw ie;
		}
	}
	
	private LoteCarga getFullLoteCargaByIdCarga (final String idCarga) {
		Carga carga = this.cargaService.getCargaFetchLotesBy(idCarga);
		LoteCarga lote = carga.getLote(this.servico);
		if (lote != null && lote.getIsExecucaoAtivada()) {
			return this.loteCargaService.findOneWithLotesClientesBy(lote.getId());
		}
		return null;
	}
}