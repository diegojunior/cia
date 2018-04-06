package br.com.totvs.cia.notificacao.service;


import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.carga.model.LoteCliente;
import br.com.totvs.cia.carga.model.StatusLoteClienteEnum;
import br.com.totvs.cia.carga.service.LoteCargaService;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.notificacao.model.NotificacaoCarga;
import br.com.totvs.cia.notificacao.model.StatusEnum;
import br.com.totvs.cia.notificacao.repository.NotificacaoCargaRepository;
import br.com.totvs.cia.notificacao.repository.NotificacaoCargaSpecification;

@Service
public class NotificacaoCargaService {
	
	private static final Logger log = Logger.getLogger(NotificacaoCargaService.class);

	@Autowired
	private NotificacaoCargaRepository notificacaoRepository;
	
	@Autowired
	private LoteCargaService loteCargaService;
	
	public NotificacaoCarga findOne(final String id) {
		return this.notificacaoRepository.findOne(id);
	}
	
	public List<NotificacaoCarga> findByIdLoteCarga(final String idLote) {
		return this.notificacaoRepository.findAll(
				NotificacaoCargaSpecification.findByLoteCarga(idLote));
	}
	
	public List<NotificacaoCarga> findBy(final List<Carga> cargas) {
		try {
			if (cargas == null || cargas.isEmpty()) {
				return Lists.newArrayList();
			}
			return this.notificacaoRepository.findByCargas(cargas);
		} catch (Exception e) {
			log.error(e);
			throw e;
		}
	}
		
	public void delete(final List<NotificacaoCarga> notificacoes) {
		this.notificacaoRepository.delete(notificacoes);
	}
	
	public void notificaInicio(final String carga, final ServicoEnum servico){
		try {
			LoteCarga lote = this.loteCargaService.findByCargaAndServicoFetchLotes(carga, servico);
			Date data = new Date();
			final StringBuilder mensagem = this.gerarMsg(lote, data);
			this.notificacaoRepository.save(new NotificacaoCarga(lote, data, StatusEnum.INICIADO, mensagem.toString()));
		} catch (final Exception e){
			throw new RuntimeException(e);
		}
	}

	public void notificaErroInterno(final String carga, final ServicoEnum servico, final String msg){
		try {
			final LoteCarga lote = this.loteCargaService.findByCargaAndServicoFetchLotes(carga, servico);
			StringBuilder sb = this.gerarMsg(lote, new Date());
			sb.append("Ocorreu um erro interno. Favor, entrar em contato com o administrador.").append("<br />")
			.append(msg).append("<br />");
			this.notificacaoRepository.save(new NotificacaoCarga(lote, StatusEnum.ERRO,  sb.toString()));
		} catch (final Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void notificaConclusao(final String carga, final ServicoEnum servico) {
		try {
			final List<NotificacaoCarga> notificacoes = Lists.newArrayList();
			final LoteCarga lote = this.loteCargaService.findByCargaAndServicoFetchLotes(carga, servico);
			List<LoteCliente> clientesComErro = this.lotesClientesComErro(lote);
			
			Date dataFinal = new Date();
			StringBuilder sb = this.gerarMsg(lote, dataFinal);
			
			if (!clientesComErro.isEmpty()) {
				notificacoes.add(new NotificacaoCarga(lote, dataFinal, StatusEnum.ERRO, sb.toString()));
			} else {
				notificacoes.add(new NotificacaoCarga(lote, dataFinal, StatusEnum.CONCLUIDO, sb.toString()));
			}
			this.notificacaoRepository.save(notificacoes);
		} catch (final Exception e){
			throw new RuntimeException(e);
		}
	}

	private List<LoteCliente> lotesClientesComErro(final LoteCarga lote) {
		return lote.getLotesClientes()
			.stream()
			.filter(loteCarteira -> StatusLoteClienteEnum.ERRO.equals(loteCarteira.getStatus()) && loteCarteira.getIsExecucaoAtivada())
			.collect(Collectors.toList());
	}
		
	private StringBuilder gerarMsg(final LoteCarga lote, final Date data) {
		final StringBuilder sb = new StringBuilder();
		String dataFinalFormatada = DateUtil.format(data, DateUtil.ddMMyyyyHHmmss);
		sb.append("Data Posição: ").append(DateUtil.format(lote.getCarga().getData(), DateUtil.ddMMyyyyHHmmss))
		.append("<br />")
		.append("Sistema: ").append(lote.getCarga().getSistema().getNome()).append("<br />")
		.append("Serviço: ").append(lote.getServico().getCodigo()).append("<br />")
		.append("Data Hora: ").append(dataFinalFormatada).append("<br />");
		
		return sb;
	}

	public void removeBy(final String carga, final ServicoEnum servico) {
		final List<NotificacaoCarga> notificacoes = this.notificacaoRepository.findByCargaAndServico(carga, servico);
		if (!notificacoes.isEmpty()) {
			this.notificacaoRepository.delete(notificacoes);
		}
	}
}