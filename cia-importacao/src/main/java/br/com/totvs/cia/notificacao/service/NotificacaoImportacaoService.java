package br.com.totvs.cia.notificacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.notificacao.model.NotificacaoImportacao;
import br.com.totvs.cia.notificacao.repository.NotificacaoImportacaoRepository;
import br.com.totvs.cia.notificacao.repository.NotificacaoImportacaoSpecification;

@Component
public class NotificacaoImportacaoService {

	@Autowired
	private NotificacaoImportacaoRepository notificacaoImportacaoRepository;

	public <S extends NotificacaoImportacao> S save(final S entity) {
		return this.notificacaoImportacaoRepository.save(entity);
	}

	public NotificacaoImportacao findOne(final String id) {
		return this.notificacaoImportacaoRepository.findOne(id);
	}

	public List<NotificacaoImportacao> findAll(final Importacao importacao) {
		return this.notificacaoImportacaoRepository.findAll(NotificacaoImportacaoSpecification.findBy(importacao),
				new Sort(Direction.ASC, "data"));
	}

	public NotificacaoImportacao findLast(final Importacao importacao) {
		final List<NotificacaoImportacao> lista = this.notificacaoImportacaoRepository
				.findAll(NotificacaoImportacaoSpecification.findBy(importacao), new Sort(Direction.DESC, "data"));
		if (!lista.isEmpty()) {
			return lista.iterator().next();
		}
		return null;
	}

	public List<NotificacaoImportacao> findAll(final List<Importacao> importacoes) {
		if (importacoes.isEmpty())
			return Lists.newArrayList();

		return this.notificacaoImportacaoRepository.findAll(NotificacaoImportacaoSpecification.findBy(importacoes),
				new Sort(Direction.ASC, "data"));
	}

	public void delete(final List<NotificacaoImportacao> notificacoes) {
		this.notificacaoImportacaoRepository.delete(notificacoes);
	}

}
