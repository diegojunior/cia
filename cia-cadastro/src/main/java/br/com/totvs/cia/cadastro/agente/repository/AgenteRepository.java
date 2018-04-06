package br.com.totvs.cia.cadastro.agente.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.cadastro.agente.model.Agente;

public interface AgenteRepository extends JpaRepository<Agente, String>, JpaSpecificationExecutor<Agente> {

	public Agente findByCodigo(String codigo);
}
