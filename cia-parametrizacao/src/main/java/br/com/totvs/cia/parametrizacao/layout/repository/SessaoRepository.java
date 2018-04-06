package br.com.totvs.cia.parametrizacao.layout.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.totvs.cia.parametrizacao.layout.model.Sessao;

public interface SessaoRepository extends JpaRepository<Sessao, String> {

	Sessao getByCodigo(final String codigo);

}
