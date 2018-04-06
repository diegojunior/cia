package br.com.totvs.cia.carga.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.carga.model.Carga;

public interface CargaRepository extends JpaRepository<Carga, String>, JpaSpecificationExecutor<Carga>{
	
	Carga findByDataAndSistema (final Date data, final SistemaEnum sistema);
	
	@Query(value = "select carga " +
			       "  from Carga carga join fetch " +
			       "	   carga.lotes lotes " +
			       " where carga.id = :id")  
	Carga findByIdFetchLotes(@Param("id") final String id);
	
}