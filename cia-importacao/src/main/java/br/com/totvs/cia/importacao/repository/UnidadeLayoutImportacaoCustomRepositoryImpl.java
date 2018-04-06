package br.com.totvs.cia.importacao.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
@Repository
public class UnidadeLayoutImportacaoCustomRepositoryImpl implements UnidadeLayoutImportacaoCustomRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Value("${hibernate.jdbc.batch_size}")
	private int batchSize;

	@Transactional
	@Override
	public List<? extends UnidadeLayoutImportacao> bulkSave(final List<? extends UnidadeLayoutImportacao> entities) {
		final List<UnidadeLayoutImportacao> unidadesSalvas = Lists.newArrayList();
		int indiceBacth = 0;
		for (final UnidadeLayoutImportacao unidade : entities) {
			unidadesSalvas.add(this.persistOrMerge(unidade));
			indiceBacth++;
			if (indiceBacth % this.batchSize == 0) {
				this.entityManager.flush();
				this.entityManager.clear();
			}
		}
		return unidadesSalvas;
	}
	
	private UnidadeLayoutImportacao persistOrMerge(final UnidadeLayoutImportacao unidade) {
		if (unidade.getId() == null) {
			this.entityManager.persist(unidade);
			return unidade;
		} else {
			return this.entityManager.merge(unidade);
		}
			
	}

}
