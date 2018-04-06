package br.com.totvs.cia.parametrizacao.perfilconciliacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.StatusPerfilEnum;

public interface PerfilConciliacaoRepository extends JpaRepository<PerfilConciliacao, String>, JpaSpecificationExecutor<PerfilConciliacao> {
	
	PerfilConciliacao findByCodigo(final String codigo);
	
	@Query("         select perfil " +
		   "           from PerfilConciliacao perfil " +
		   "     join fetch perfil.configuracao " +
		   "left join fetch perfil.regras " +
		   "          where perfil.codigo = :codigo")
	PerfilConciliacao findByCodigoFetchConfiguracaoAndRegras(@Param("codigo")final String codigo);

	List<PerfilConciliacao> findByStatusOrderByCodigoAsc(final StatusPerfilEnum status);
	
}