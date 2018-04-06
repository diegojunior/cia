package br.com.totvs.cia.cadastro.equivalencia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.cadastro.equivalencia.model.TipoEquivalencia;

public interface TipoEquivalenciaRepository extends JpaRepository<TipoEquivalencia, String>, JpaSpecificationExecutor<TipoEquivalencia>{

}
