package br.com.totvs.cia.parametrizacao.unidade.importacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.parametrizacao.unidade.importacao.model.LinhaBlocoParametrizacaoUnidadeImportacao;

public interface LinhaBlocoParametrizacaoUnidadeImportacaoRepository extends JpaRepository<LinhaBlocoParametrizacaoUnidadeImportacao, String>, JpaSpecificationExecutor<LinhaBlocoParametrizacaoUnidadeImportacao> {

}