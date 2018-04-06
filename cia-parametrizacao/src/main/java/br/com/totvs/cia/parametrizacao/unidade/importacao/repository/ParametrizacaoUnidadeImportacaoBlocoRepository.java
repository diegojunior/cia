package br.com.totvs.cia.parametrizacao.unidade.importacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;

public interface ParametrizacaoUnidadeImportacaoBlocoRepository extends JpaRepository<ParametrizacaoUnidadeImportacaoBloco, String>, JpaSpecificationExecutor<ParametrizacaoUnidadeImportacaoBloco> {
	
	List<ParametrizacaoUnidadeImportacaoBloco> findByCodigo(final String codigo);
	
	@Query("select parametrizacao from ParametrizacaoUnidadeImportacaoBloco parametrizacao where parametrizacao.layout.id = :idLayout")
	List<ParametrizacaoUnidadeImportacaoBloco> findByLayout(@Param("idLayout")final String idLayout);
	
	ParametrizacaoUnidadeImportacaoBloco getByCodigo(final String codigo);
	
	@Query("     select bloco " +
		   "       from ParametrizacaoUnidadeImportacaoBloco bloco " +
		   " join fetch bloco.linhas linha " +
		   "      where bloco.id = :id ")
	ParametrizacaoUnidadeImportacaoBloco findOneFetchLinhas(@Param("id") final String id);
}