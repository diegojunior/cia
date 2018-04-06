package br.com.totvs.cia.parametrizacao.perfilconciliacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.ConfiguracaoPerfilConciliacao;

public interface ConfiguracaoPerfilConciliacaoRepository extends JpaRepository<ConfiguracaoPerfilConciliacao, String>, JpaSpecificationExecutor<ConfiguracaoPerfilConciliacao> {
	
	@Query(value = "select configuracao " +
		       	   "  from ConfiguracaoPerfilConciliacao configuracao join fetch " +
		       	   "	   	   configuracao.campos campos " +
		       	   " where configuracao.perfil.id = :perfil")  
	List<ConfiguracaoPerfilConciliacao> getByPerfil(@Param("perfil") final String perfil);
	
}