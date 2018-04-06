package br.com.totvs.cia.parametrizacao.layout.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.parametrizacao.layout.model.AbstractLayout;

public interface LayoutRepository extends JpaRepository<AbstractLayout, String>, JpaSpecificationExecutor<AbstractLayout> {

}