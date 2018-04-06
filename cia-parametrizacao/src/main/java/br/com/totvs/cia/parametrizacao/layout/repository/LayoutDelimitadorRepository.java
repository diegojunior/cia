package br.com.totvs.cia.parametrizacao.layout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;

public interface LayoutDelimitadorRepository extends JpaRepository<LayoutDelimitador, String>, JpaSpecificationExecutor<LayoutDelimitador> {
	
	public LayoutDelimitador findByCodigo(final String codigo);

	public List<LayoutDelimitador> findByStatus(final StatusLayoutEnum status);
}
