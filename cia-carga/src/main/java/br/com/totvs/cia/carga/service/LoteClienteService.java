package br.com.totvs.cia.carga.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.carga.model.LoteCliente;
import br.com.totvs.cia.carga.model.StatusLoteClienteEnum;
import br.com.totvs.cia.carga.repository.LoteClienteRepository;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.exception.CiaBusinessException;
import br.com.totvs.cia.infra.exception.JobException;

@Service
public class LoteClienteService {
	
	private static final Logger log = Logger.getLogger(LoteClienteService.class);
	
	@Autowired
	private LoteClienteRepository repository;

	public LoteCliente save(final LoteCliente lote){
		return this.repository.saveAndFlush(lote);
	}
	
	public void save(final List<LoteCliente> lotes){
		for (LoteCliente lote : lotes) {
			this.repository.save(lote);
		}
	}
	
	@Transactional
	public LoteCliente criaEstruturaCliente(final LoteCarga loteCarga, final String codigoCliente, final StatusLoteClienteEnum status) {
		try {
			return this.repository.save(new LoteCliente(loteCarga, codigoCliente, status));
		} catch (Exception ex) {
			throw new CiaBusinessException("Nâo foi possível criar o Lote do Cliente", ex);
		}
	}

	public LoteCliente findOne(final String id) {
		return this.repository.findOne(id);
	}
	
	public LoteCliente findBy(final String cliente, final LoteCarga loteCarga) {
		try {
			return this.repository.findByClienteAndLoteCarga(cliente, loteCarga.getId());
		} catch (Exception ex) {
			log.error("Nâo foi possível buscar o Lote do Cliente", ex);
			return null;
		}
	}
	
	public List<LoteCliente> findListWithUnidadesBy(String idLoteCarga) {
		return this.repository.findByLoteCargaFetchUnidades(idLoteCarga);
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public List<LoteCliente> criaLotesClienteInexistentes (final LoteCarga lote, final List<UnidadeProcessamentoJson> unidades) {
		try {
			List<LoteCliente> lotesClientes = Lists.newArrayList();
			for (UnidadeProcessamentoJson unidade : unidades) {
				Boolean isLoteExistente = this.contains (lotesClientes, unidade.getCliente());
				if (!isLoteExistente) {
					LoteCliente loteCliente = new LoteCliente(lote, unidade.getCliente(), StatusLoteClienteEnum.SUCESSO);
					LoteCliente loteClienteCriado = this.repository.saveAndFlush(loteCliente);
					lotesClientes.add(loteClienteCriado);
				}
			}
			return lotesClientes;
		} catch (Exception ex) {
			log.error("Não foi possível criar os lotes de Clientes", ex);
			throw new JobException(ex);
		}
	}
	
	public Boolean contains(final List<LoteCliente> lotesClientes, final String cliente) {
		for (LoteCliente lote : lotesClientes) {
			if (lote.getCliente().equals(cliente)) {
				return true;
			}
		}
		return false;
	}
	
	public void verificaEAtualizaStatus(final List<LoteCliente> lotes, final List<UnidadeProcessamentoJson> unidades) {
		for (LoteCliente loteCliente : lotes) {
			this.verificaEAtualizaStatus(loteCliente, unidades);
		}
	}
	
	public void verificaEAtualizaStatus(final LoteCliente lote, final List<UnidadeProcessamentoJson> unidades) {
		UnidadeProcessamentoJson unidadeEncontrada = 
				unidades.stream()
						.filter(unidade -> unidade.getCliente() != null && unidade.getCliente().equals(lote.getCliente()))
						.findAny()
						.get();
		if (unidadeEncontrada != null) {
			lote.setStatus(StatusLoteClienteEnum.SUCESSO);
		} else {
			lote.setStatus(StatusLoteClienteEnum.ATENCAO);
		}
		this.repository.save(lote);
	}
	
	public void atualizaStatusComSucesso(final LoteCliente lote) {
		lote.setStatus(StatusLoteClienteEnum.SUCESSO);
		this.repository.save(lote);
	}

	public void atualizaStatusComWarning(final LoteCliente lote) {
		lote.setStatus(StatusLoteClienteEnum.ATENCAO);
		this.repository.save(lote);
	}
	
	public void atualizaStatusComErro(final LoteCliente lote) {
		lote.setStatus(StatusLoteClienteEnum.ERRO);
		this.repository.save(lote);
	}
	
	public void atualizaStatusComErro(final List<LoteCliente> lotes) {
		if (lotes != null) {
			for (LoteCliente loteCliente : lotes) {
				loteCliente.setStatus(StatusLoteClienteEnum.ERRO);
			}
			this.repository.save(lotes);
		}
	}
	
	public void atualizaLotesClientesParaErroBy(final LoteCarga lote) {
		List<LoteCliente> lotesClientes = this.findListWithUnidadesBy(lote.getId());
		if (lotesClientes != null) {
			for (final LoteCliente loteCliente : lotesClientes) {
				if (loteCliente.getIsExecucaoAtivada() && loteCliente.contemUnidades()) {
					loteCliente.setStatus(StatusLoteClienteEnum.SUCESSO);
				} else if (loteCliente.getIsExecucaoAtivada() && !loteCliente.contemUnidades()) {
					loteCliente.setStatus(StatusLoteClienteEnum.ERRO);
				}
			}
			lote.atualizaStatusLotesClientes(lotesClientes);
		}
	}

	public void atualizaLotesClientesParaSucessoBy(final LoteCarga lote) {
		List<LoteCliente> lotesClientes = this.findListWithUnidadesBy(lote.getId());
		if (lotesClientes != null) {
			for (final LoteCliente loteCliente : lotesClientes) {
				if (loteCliente.getIsExecucaoAtivada() && loteCliente.contemUnidades()) {
					loteCliente.setStatus(StatusLoteClienteEnum.SUCESSO);
				} else if (loteCliente.getIsExecucaoAtivada() && !loteCliente.contemUnidades()) {
					loteCliente.setStatus(StatusLoteClienteEnum.ATENCAO);
				}
			}
			lote.atualizaStatusLotesClientes(lotesClientes);
		}
	}

	public void atualizaStatusLotesClientesBy(final LoteCarga lote) {
		List<LoteCliente> lotesClientes = this.findListWithUnidadesBy(lote.getId());
		if (lotesClientes != null) {
			for (final LoteCliente loteCliente : lotesClientes) {
				if (loteCliente.getIsExecucaoAtivada() && loteCliente.contemUnidades()) {
					loteCliente.setStatus(StatusLoteClienteEnum.SUCESSO);
				} else if (loteCliente.getIsExecucaoAtivada() && !loteCliente.contemUnidades()) {
					loteCliente.setStatus(StatusLoteClienteEnum.ATENCAO);
				}
			}
			lote.atualizaStatusLotesClientes(lotesClientes);
		}
	}
}