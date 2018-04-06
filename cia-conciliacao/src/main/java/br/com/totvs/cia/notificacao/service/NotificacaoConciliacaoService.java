package br.com.totvs.cia.notificacao.service;

import static br.com.totvs.cia.notificacao.repository.NotificacaoConciliacaoSpecification.findBy;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.model.StatusConciliacaoEnum;
import br.com.totvs.cia.notificacao.model.NotificacaoConciliacao;
import br.com.totvs.cia.notificacao.repository.NotificacaoConciliacaoRepository;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;

@Service
public class NotificacaoConciliacaoService {

	@Autowired
	private NotificacaoConciliacaoRepository notificacaoConciliacaoRepository;
	
	
	public <S extends NotificacaoConciliacao> S save(final S entity) {
		return this.notificacaoConciliacaoRepository.save(entity);
	}
	
	public <S extends NotificacaoConciliacao> List<S> save(final Iterable<S> entities) {
		return this.notificacaoConciliacaoRepository.save(entities);
	}
	
	public void delete(final Iterable<? extends NotificacaoConciliacao> entities) {
		this.notificacaoConciliacaoRepository.delete(entities);
	}

	public List<NotificacaoConciliacao> findAll(final List<Conciliacao> conciliacoes) {
		return this.notificacaoConciliacaoRepository
				.findAll(findBy(conciliacoes));
	}

	public List<NotificacaoConciliacao> findAll(final Date data, final List<PerfilConciliacao> perfis,
			final StatusConciliacaoEnum status) {
		return this.notificacaoConciliacaoRepository.findAll(findBy(data, perfis, status));
	}
	
	public void deleteNotificacoes(final Conciliacao conciliacao) {
		this.notificacaoConciliacaoRepository.deleteNotificacoesByConciliacao(conciliacao);
	}
	
}
