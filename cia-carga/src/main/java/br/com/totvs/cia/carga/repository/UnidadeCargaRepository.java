package br.com.totvs.cia.carga.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.carga.model.UnidadeCarga;

public interface UnidadeCargaRepository extends JpaRepository<UnidadeCarga, String>, JpaSpecificationExecutor<UnidadeCarga>{
	
}