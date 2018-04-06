package br.com.totvs.cia.cadastro.equivalencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.cadastro.equivalencia.model.Remetente;

public interface RemetenteRepository extends JpaRepository<Remetente, String>, JpaSpecificationExecutor<Remetente>{

}
