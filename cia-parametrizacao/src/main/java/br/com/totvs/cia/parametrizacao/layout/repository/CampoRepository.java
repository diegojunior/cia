package br.com.totvs.cia.parametrizacao.layout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.totvs.cia.parametrizacao.layout.model.Campo;

public interface CampoRepository extends JpaRepository<Campo, String> {
}
