package br.com.totvs.cia.parametrizacao.transformacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;

public interface TransformacaoRepository extends JpaRepository<Transformacao, String>, JpaSpecificationExecutor<Transformacao>{

	@Query("select "
			+ "case when (count(importacao) > 0) "
			+ "then true "
			+ "else false end "
			+ "from Importacao importacao where importacao.layout = :layout")
	Boolean existeImportacaoParaLayout(@Param("layout")final Layout layout);
	
	@Query("select "
			+ "case when (count(importacao) > 0) "
			+ "then true "
			+ "else false end "
			+ "from Importacao importacao where importacao.layout in ( "
			+ "select transformacao.layout from Transformacao transformacao)")
	Boolean existeImportacaoParaTransformacao();
	
}