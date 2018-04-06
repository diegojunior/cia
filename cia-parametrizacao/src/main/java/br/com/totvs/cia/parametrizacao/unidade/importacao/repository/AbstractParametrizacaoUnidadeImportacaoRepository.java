	package br.com.totvs.cia.parametrizacao.unidade.importacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.parametrizacao.unidade.importacao.model.AbstractParametrizacaoUnidadeImportacao;

public interface AbstractParametrizacaoUnidadeImportacaoRepository extends JpaRepository<AbstractParametrizacaoUnidadeImportacao, String>, 
	JpaSpecificationExecutor<AbstractParametrizacaoUnidadeImportacao> {
	
	@Query(value = "select parametrizacao " +
			       "  from AbstractParametrizacaoUnidadeImportacao parametrizacao inner join " +
			       "	   parametrizacao.layout layout " +
			       " where layout.id = :id ")
	List<AbstractParametrizacaoUnidadeImportacao> listByLayout(@Param("id") String layout);

	AbstractParametrizacaoUnidadeImportacao findByCodigo(final String codigo);
}