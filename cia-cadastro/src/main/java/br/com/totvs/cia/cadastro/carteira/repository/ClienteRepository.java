package br.com.totvs.cia.cadastro.carteira.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.cadastro.carteira.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, String>, JpaSpecificationExecutor<Cliente>{
	
	Cliente findByCodigo(final String codigo);

}
