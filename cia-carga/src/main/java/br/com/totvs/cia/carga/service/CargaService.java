package br.com.totvs.cia.carga.service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.carga.json.TipoExecucaoCargaJsonEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.carga.model.StatusCargaEnum;
import br.com.totvs.cia.carga.repository.CargaRepository;
import br.com.totvs.cia.carga.repository.CargaSpecification;

@Service
public class CargaService {
	
	@Autowired
	private ExecucaoPorClienteService execucaoPorCliente;
	
	@Autowired
	private ExecucaoPorGrupoService execucaoPorGrupo;
	
	@Autowired
	private ExecucaoPorTodosService execucaoPorTodos;
	
	@Autowired
	private LoteCargaService loteCargaService;
	
	@Autowired
	private CargaRepository cargaRepository;
	
	public List<Carga> search(final Date data, final SistemaEnum sistema, 
			final StatusCargaEnum status) {
		return this.cargaRepository.findAll(CargaSpecification.search(data, sistema, status), new Sort(Sort.Direction.ASC, "data"));
	}
	
	public List<Carga> listBy(final Date dataCarga, final SistemaEnum sistema, final String codigoServico) {
		final List<LoteCarga> lotesCarga = this.loteCargaService.listBy(dataCarga, sistema, codigoServico);
		
		if (lotesCarga.isEmpty()) {
			return Lists.newArrayList();
		}
		
		return lotesCarga.stream().map(lote -> lote.getCarga()).distinct().collect(Collectors.toList());
	}
	
	public Carga findBy(final String id) {
		return id != null ? this.cargaRepository.findOne(id) : null;
	}
	
	public Carga getCargaFetchLotesBy(final String id) {
		return this.cargaRepository.findByIdFetchLotes(id);
	}
	
	public Carga findBy(final Date data, final SistemaEnum sistema) {
		return this.cargaRepository.findByDataAndSistema(data, sistema);
	}
	
	public void executa(final Carga cargaParam, final TipoExecucaoCargaJsonEnum tipoExecucao) {
		if (tipoExecucao.isCliente()) {
			this.execucaoPorCliente.executa(cargaParam);
		}
		if (tipoExecucao.isGrupo()) {
			this.execucaoPorGrupo.executa(cargaParam);
		}
		if (tipoExecucao.isTodos()) {
			this.execucaoPorTodos.executa(cargaParam);
		}
	}
	
	public Boolean possuiLotesEmAndamento(final String idCarga) {
		Carga carga = this.getCargaFetchLotesBy(idCarga);
		return carga.isLotesEmAndamento();
	}

	public void concluirComErro(final String id) {
		final Carga carga = this.cargaRepository.findByIdFetchLotes(id);
		carga.setStatus(StatusCargaEnum.ERRO);
		this.cargaRepository.save(carga);
	}

	public void concluirComSucesso(final String id) {
		final Carga carga = this.cargaRepository.findByIdFetchLotes(id);
		carga.setStatus(StatusCargaEnum.CONCLUIDO);
		this.cargaRepository.save(carga);
	}

	public void concluir(String id) {
		final Carga carga = this.cargaRepository.findByIdFetchLotes(id);
		carga.setStatus(StatusCargaEnum.CONCLUIDO);
		this.cargaRepository.save(carga);
	}
}