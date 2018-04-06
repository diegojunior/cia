package br.com.totvs.cia.parametrizacao.layout.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;

public interface LayoutXmlRepository extends JpaRepository<LayoutXml, String>, JpaSpecificationExecutor<LayoutXml> {

	public LayoutXml findByCodigo(final String codigo);

	public List<LayoutXml> findByStatus(final StatusLayoutEnum status);
}
