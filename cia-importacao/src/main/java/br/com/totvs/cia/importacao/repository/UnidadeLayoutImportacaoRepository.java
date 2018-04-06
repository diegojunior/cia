package br.com.totvs.cia.importacao.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;

public interface UnidadeLayoutImportacaoRepository extends PagingAndSortingRepository<UnidadeLayoutImportacao, String> {

	@Query("select unidade from UnidadeLayoutImportacao unidade join fetch unidade.campos where unidade.importacao = :importacao")
	public List<UnidadeLayoutImportacao> getUnidadesByImportacao(@Param("importacao")Importacao importacao, Pageable pageRequest);
	
	@Query("select unidade from UnidadeLayoutImportacao unidade join fetch unidade.campos where unidade.importacao = :importacao order by unidade.numeroLinha")
	public List<UnidadeLayoutImportacao> obtemUnidadesOrdenadasPeloNumeroLinha(@Param("importacao")Importacao importacao, Pageable pageRequest);
	
	@Query("select unidade from UnidadeLayoutImportacao unidade join fetch unidade.campos where unidade.importacao = :importacao and unidade not in (:unidadesParametrizadas)")
	public List<UnidadeLayoutImportacao> getUnidadesPorImportacaoSemParametrizacao(@Param("importacao")final Importacao importacao, 
				@Param("unidadesParametrizadas")List<UnidadeLayoutImportacao> unidadesParametrizadas, 
				final Pageable pageRequest);
	@Transactional
	@Modifying
	@Query("delete from CampoLayoutImportacao campo where campo.unidade in ( " +
	 "select unidade from UnidadeLayoutImportacao unidade where unidade.importacao = :importacao)")
	public void removeCamposLayoutPor(@Param("importacao")Importacao importacao);
	
	@Transactional
	@Modifying
	@Query("delete from UnidadeLayoutImportacao unidade where unidade.importacao = :importacao")
	public void removeUnidadeLayoutPorImportacao(@Param("importacao")Importacao importacao);
	
}
