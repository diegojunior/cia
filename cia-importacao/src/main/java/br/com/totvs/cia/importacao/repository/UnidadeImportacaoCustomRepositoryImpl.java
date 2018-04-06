package br.com.totvs.cia.importacao.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import br.com.totvs.cia.importacao.model.UnidadeImportacao;
@Repository
public class UnidadeImportacaoCustomRepositoryImpl implements UnidadeImportacaoCustomRepository {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Value("${hibernate.jdbc.batch_size}")
	private int batchSize;

	@Transactional
	@Override
	public List<? extends UnidadeImportacao> bulkSave(final List<? extends UnidadeImportacao> entities) {
		final List<UnidadeImportacao> unidadesSalvas = Lists.newArrayList();
		int indiceBacth = 0;
		for (final UnidadeImportacao unidade : entities) {
			unidadesSalvas.add(this.persistOrMerge(unidade));
			indiceBacth++;
			if (indiceBacth % this.batchSize == 0) {
				this.entityManager.flush();
				this.entityManager.clear();
			}
		}
		return unidadesSalvas;
	}
	
	private UnidadeImportacao persistOrMerge(final UnidadeImportacao unidade) {
		if (unidade.getId() == null) {
			this.entityManager.persist(unidade);
			return unidade;
		} else {
			return this.entityManager.merge(unidade);
		}
			
	}

}
