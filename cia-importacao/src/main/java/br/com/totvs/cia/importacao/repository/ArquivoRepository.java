package br.com.totvs.cia.importacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.totvs.cia.importacao.model.Arquivo;
public interface ArquivoRepository extends JpaRepository<Arquivo, String> {

}
