package br.com.totvs.cia.carga.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.totvs.cia.carga.model.LoteCliente;

public interface LoteClienteRepository extends JpaRepository<LoteCliente, String>, JpaSpecificationExecutor<LoteCliente> {
	
	@Query(value = "select loteCliente " +
	              "   from LoteCliente loteCliente inner join loteCliente.loteCarga loteCarga " +
	              "        left join fetch loteCliente.unidades unidades " +
	              "  where loteCarga.id = :idLoteCarga ")  
	List<LoteCliente> findByLoteCargaFetchUnidades(@Param("idLoteCarga") final String idLoteCarga);

	@Query(value = "select loteCliente " +
		           "  from LoteCliente loteCliente inner join loteCliente.loteCarga loteCarga " +
		           " where loteCarga.id = :idLoteCarga " +
		           "   and loteCliente.cliente = :cliente ") 
	LoteCliente findByClienteAndLoteCarga(@Param("cliente") final String cliente, @Param("idLoteCarga") final String idLoteCarga);
	
}