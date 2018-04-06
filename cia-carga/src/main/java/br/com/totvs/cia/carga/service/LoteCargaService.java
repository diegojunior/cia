package br.com.totvs.cia.carga.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.service.ConfiguracaoServicoService;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.carga.repository.LoteCargaSpecification;
import br.com.totvs.cia.carga.repository.LoteRepository;

@Service
public class LoteCargaService {
	
	private static final Logger log = Logger.getLogger(LoteCargaService.class);

	@Autowired
	private LoteRepository loteRepository;
	
	@Autowired
	private LoteClienteService loteClienteService;
	
	@Autowired
	private ConfiguracaoServicoService configuracaoServicoService;

	public LoteCarga save(final LoteCarga lote){
		return this.loteRepository.saveAndFlush(lote);
	}

	public LoteCarga findOne(final String id) {
		return this.loteRepository.findOne(id);
	}
	
	public LoteCarga findOneFetchCamposServico(final String id) {
		try {
			LoteCarga lote = this.loteRepository.findByIdFetchLotes(id);
			lote.setServico(this.configuracaoServicoService.findOneFetchCampos(lote.getServico().getId()));
			return lote;
		} catch (Exception ex) {
			log.error("Não foi possivel carregar o Lote de Carga por ID");
			throw new RuntimeException(ex);
		}
	}
	
	public List<LoteCarga> findLoteToProcessWithLoteClienteBy(final String idCarga) {
		return this.loteRepository.findAll(LoteCargaSpecification.findLoteToProcessWithLoteCarteiraBy(idCarga));
	}
	
	public List<LoteCarga> listBy(final Date dataCarga, final SistemaEnum sistema, final String codigoServico) {
		return this.loteRepository.findAll(LoteCargaSpecification.findBy(dataCarga, sistema, codigoServico));
	}
	
	public LoteCarga findOneWithLotesClientesBy(String id) {
		return this.loteRepository.findByIdFetchLotes(id);
	}
	
	public LoteCarga findByCargaAndServicoFetchLotes(final String carga, final ServicoEnum servico) {
		return this.loteRepository.findByCargaAndServicoFetchLotes(carga, servico);
	}
	
	public LoteCarga findByCargaAndServico(final String carga, final ServicoEnum servico) {
		return this.loteRepository.findByCargaAndServico(carga, servico);
	}
	
	public void concluirComErro(final String carga, final ServicoEnum servico) {
		LoteCarga lote = this.findByCargaAndServicoFetchLotes(carga, servico);
		lote.atualizaStatusParaErro();
		this.loteClienteService.atualizaLotesClientesParaErroBy(lote);
		lote.inativaExecucao();
		this.save(lote);
	}

	public void concluirComSucesso(final String carga, final ServicoEnum servico) {
		LoteCarga lote = this.findByCargaAndServicoFetchLotes(carga, servico);
		lote.atualizaStatusParaSucesso();
		this.loteClienteService.atualizaStatusLotesClientesBy(lote);
		lote.inativaExecucao();
		this.save(lote);
	}
}