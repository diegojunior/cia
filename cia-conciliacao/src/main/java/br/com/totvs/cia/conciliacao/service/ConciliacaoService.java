
package br.com.totvs.cia.conciliacao.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.model.ConciliacaoForExecution;
import br.com.totvs.cia.conciliacao.model.ConciliacoesForExecution;
import br.com.totvs.cia.conciliacao.model.LoteConciliacao;
import br.com.totvs.cia.conciliacao.model.StatusConciliacaoEnum;
import br.com.totvs.cia.conciliacao.repository.CampoChaveConciliacaoRepository;
import br.com.totvs.cia.conciliacao.repository.CampoConciliacaoRepository;
import br.com.totvs.cia.conciliacao.repository.CampoInformativoRepository;
import br.com.totvs.cia.conciliacao.repository.ConciliacaoRepository;
import br.com.totvs.cia.conciliacao.repository.ConciliacaoSpecification;
import br.com.totvs.cia.conciliacao.repository.LoteConciliacaoRepository;
import br.com.totvs.cia.conciliacao.repository.UnidadeConciliacaoRepository;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.notificacao.service.NotificacaoConciliacaoService;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;

@Service
public class ConciliacaoService {

	private static final Logger log = Logger.getLogger(ConciliacaoService.class);
	
	@Autowired
	private ConciliacaoRepository conciliacaoRepository;
	
	@Autowired
	private NotificacaoConciliacaoService notificacaoConciliacaoService;
	
	@Autowired
	private CampoChaveConciliacaoRepository campoChaveConciliacaoRepository;
	
	@Autowired
	private CampoConciliacaoRepository campoConciliacaoRepository;
	
	@Autowired
	private CampoInformativoRepository campoInformativoRepository;
	
	@Autowired
	private UnidadeConciliacaoRepository unidadeConciliacaoRepository;
	
	@Autowired
	private LoteConciliacaoRepository loteConciliacaoRepository;

	
	public Conciliacao save(final Conciliacao entity) {
		return this.conciliacaoRepository.save(entity);
	}

	public void delete(final Conciliacao entity) {
		this.conciliacaoRepository.delete(entity);
	}
	
	public void deleteConciliacaoNaoGravadas(final Conciliacao entity) {
		log.info("Removendo conciliações não gravadas.");
		LoteConciliacao lote = entity.getLote();
		if (!lote.getUnidades().isEmpty()) {
			this.campoChaveConciliacaoRepository.deletePorUnidadeConciliacao(lote.getUnidades());
			this.campoConciliacaoRepository.deletePorUnidadeConciliacao(lote.getUnidades());
			this.campoInformativoRepository.deletePorUnidadeConciliacao(lote.getUnidades());
		}
		this.unidadeConciliacaoRepository.deletePorLoteConciliacao(lote);
		this.loteConciliacaoRepository.deleteOne(lote.getId());
		this.conciliacaoRepository.deleteUnic(entity.getId());
	}
	
	public List<Conciliacao> search(final Date data, final String perfil, final StatusConciliacaoEnum status) {
		return this.conciliacaoRepository.findAll(ConciliacaoSpecification.search(data, perfil, status));
	}
	
	public List<Conciliacao> listConciliacao(final Date data, final PerfilConciliacao perfilConciliacao) {
		return this.conciliacaoRepository.findConciliacao(data, perfilConciliacao.getId());
	}

	public Conciliacao getFullBy(final Date data, final String perfil) {
		final Conciliacao conciliacao = this.conciliacaoRepository.findByDataAndIdPerfil(data, perfil);
		return conciliacao;
	}
	
	public List<ConciliacaoForExecution> buscaConciliacoesParaExecucao(final Date data, final String perfil) {
		try {
			final List<ConciliacaoForExecution> conciliacoesForExecution = this.conciliacaoRepository.findByProjectedColumns(data);
			return new ConciliacoesForExecution(conciliacoesForExecution).getBy(perfil);
		} catch (final Exception e) {
			log.error(e);
			throw e;
		}
	}
	
	public Conciliacao gravar(final Conciliacao conciliacao) {

		conciliacao.setDataGravacao(new Date());

		if (conciliacao.isDivergente()) {
			conciliacao.setStatus(StatusConciliacaoEnum.GRAVADA_DIVERGENCIA);
		} else {
			conciliacao.setStatus(StatusConciliacaoEnum.GRAVADA);
		}

		return this.conciliacaoRepository.save(conciliacao);
	}
	
	public void removeConciliacoesNaoGravadas(final Date data, final PerfilConciliacao perfil) {
		log.info("Removendo as conciliações que não estao gravadas com base na Data e Perfil a ser executado.");
		final List<Conciliacao> conciliacoes = this.listConciliacao(data, perfil);

		for (final Conciliacao conciliacao : conciliacoes) {

			final Boolean isConciliacaoNaoGravadaExistente = !conciliacao.isGravada();
			
			if(isConciliacaoNaoGravadaExistente) {
				this.removeNotificacoes(conciliacao);
				this.deleteConciliacaoNaoGravadas(conciliacao);
			}
		}
	}
	
	@Transactional
	public Conciliacao iniciarConciliacao(final Conciliacao conciliacao) {
		log.info("Incluindo o Status ANDAMENTO na inicialização da conciliação");
		conciliacao.comStatus(StatusConciliacaoEnum.ANDAMENTO);
		return this.conciliacaoRepository.save(conciliacao);
	}
	
	private void removeNotificacoes(final Conciliacao conciliacao) {
		log.info("Removendo notificações das conciliações não gravadas." + conciliacao.getId());
		this.notificacaoConciliacaoService.deleteNotificacoes(conciliacao);
	}

	public Conciliacao getConciliacaoPorImportacao(final Importacao importacao) {
		final List<Conciliacao> conciliacoes = this.conciliacaoRepository
				.findAll(ConciliacaoSpecification.getPorImportacao(importacao.getId()));
		if (!conciliacoes.isEmpty()) {
			return conciliacoes.iterator().next();
		}
		return null;
	}
}