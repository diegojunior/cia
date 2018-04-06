package br.com.totvs.cia.parametrizacao.layout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;

public interface LayoutPosicionalRepository extends JpaRepository<LayoutPosicional, String>, JpaSpecificationExecutor<LayoutPosicional> {
	
	public LayoutPosicional findByCodigo(final String codigo);
	
	public List<LayoutPosicional> findByStatus(final StatusLayoutEnum status);
}
