package br.com.totvs.cia.parametrizacao.dominio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
public interface DominioRepository extends JpaRepository<Dominio, String>, JpaSpecificationExecutor<Dominio>{
	
}