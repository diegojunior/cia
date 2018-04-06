package br.com.totvs.cia.importacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.UnidadeImportacao;

public interface UnidadeImportacaoRepository extends JpaRepository<UnidadeImportacao, String>, JpaSpecificationExecutor<UnidadeImportacao> {

	@Transactional
	@Modifying
	@Query("delete from CampoImportacao campo where campo.unidade in ( "+ 
	"select unidade from UnidadeImportacao unidade where unidade.importacao = :importacao)")
	public void deletarCamposUnidadesPelaImportacao(@Param("importacao")Importacao importacao);
	
	@Transactional
	@Modifying
	@Query("delete from UnidadeImportacao unidade where unidade.importacao = :importacao")
	public void deletarUnidadesPelaImportacao(@Param("importacao")Importacao importacao);
	
}
