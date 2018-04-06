package br.com.totvs.cia.carga.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.carga.json.TipoExecucaoCargaJsonEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.carga.model.StatusCargaEnum;
import br.com.totvs.cia.carga.model.StatusLoteCargaEnum;
import br.com.totvs.cia.carga.repository.CargaRepository;

@Service
public class ExecucaoPorClienteService {
	
	@Autowired
	private CargaRunJobService cargaJobService;
	
	@Autowired
	private CargaRepository cargaRepository;
	
	public void executa(final Carga cargaParam) {
		final Carga cargaConfigurada = this.verificaEConfiguraCarga(cargaParam);
		final Carga carga = this.cargaRepository.save(cargaConfigurada);
		this.cargaJobService.run(carga, TipoExecucaoCargaJsonEnum.CLIENTE);
	}

	private Carga verificaEConfiguraCarga(final Carga cargaParam) {
		final Carga cargaExistente = this.findBy(cargaParam.getData(), cargaParam.getSistema());
		if(cargaExistente != null) {
			cargaExistente.setStatus(StatusCargaEnum.ANDAMENTO);
			return this.configuraCargaExistente(cargaExistente, cargaParam.getLotes());
		}
		return cargaParam;
	}
	
	private Carga configuraCargaExistente(final Carga carga, final List<LoteCarga> lotes) {
		for (final LoteCarga lote : lotes) {
			final LoteCarga loteCarga = carga.getLote(lote.getServico());
			if (loteCarga != null) {
				loteCarga.setStatus(StatusLoteCargaEnum.ANDAMENTO);
				carga.criaNovosLotesClientes(lote);
				carga.ativaReexecucao(lote);
			} else {
				lote.setCarga(carga);
				carga.getLotes().add(lote);
			}
		}
		return carga;
	}	
	
	public Carga findBy(final String id) {
		return id != null ? this.cargaRepository.findOne(id) : null;
	}
		
	public Carga findBy(final Date data, final SistemaEnum sistema) {
		return this.cargaRepository.findByDataAndSistema(data, sistema);
	}
}