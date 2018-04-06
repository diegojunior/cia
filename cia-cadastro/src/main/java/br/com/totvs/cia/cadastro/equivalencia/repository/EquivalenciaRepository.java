package br.com.totvs.cia.cadastro.equivalencia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.cadastro.equivalencia.model.Equivalencia;

public interface EquivalenciaRepository extends JpaRepository<Equivalencia, String>, JpaSpecificationExecutor<Equivalencia> {

	@Query("select e from Equivalencia e where e.idLegado in (:idsLegados)")
	public List<Equivalencia> getByIdsLegado(@Param("idsLegados")List<String> idLegado);

	public Equivalencia getByIdLegado(String idLegado);

}
