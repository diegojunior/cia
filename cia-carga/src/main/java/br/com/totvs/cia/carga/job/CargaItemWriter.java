package br.com.totvs.cia.carga.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.carga.model.LoteCliente;
import br.com.totvs.cia.carga.model.LotesClientes;
import br.com.totvs.cia.carga.model.UnidadeCarga;
import br.com.totvs.cia.carga.service.LoteClienteService;
import br.com.totvs.cia.carga.service.UnidadeCargaService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CargaItemWriter implements ItemWriter<UnidadeCarga>{
	
	private static final Logger log = Logger.getLogger(CargaItemWriter.class);
	
	private LoteClienteService loteClienteService;
	
	private UnidadeCargaService unidadeCargaService;
	
	@Autowired
	public CargaItemWriter(final LoteClienteService loteClienteService, final UnidadeCargaService unidadeCargaService) {
		this.loteClienteService = loteClienteService;
		this.unidadeCargaService = unidadeCargaService;
	}
	
	@Override
	public void write(final List<? extends UnidadeCarga> items) throws Exception {
		try {
			LotesClientes lotesClientes = LotesClientes.build();
			if (items != null && !items.isEmpty()) {
				
				for (UnidadeCarga unidade : items) {
					
					if (!lotesClientes.contains(unidade.getLoteCliente())) {
						
						LoteCliente loteCliente = this.loteClienteService.findBy(unidade.getLoteCliente().getCliente(), unidade.getLoteCliente().getLoteCarga());
						if (loteCliente == null) {
							loteCliente = this.loteClienteService.save(unidade.getLoteCliente());
						}
						unidade.setLoteCliente(loteCliente);
						lotesClientes.add(loteCliente);
						
					} else {
						
						LoteCliente loteCliente = lotesClientes.get(unidade.getLoteCliente().getCliente(), unidade.getLoteCliente().getLoteCarga());
						unidade.setLoteCliente(loteCliente);
						
					}
				}
				
				this.unidadeCargaService.saveAll(items);
			}
		} catch (Exception ex) {
			log.error("Não foi possível inserit as Unidades de Carga.", ex);
			throw ex;
		}
	}
}