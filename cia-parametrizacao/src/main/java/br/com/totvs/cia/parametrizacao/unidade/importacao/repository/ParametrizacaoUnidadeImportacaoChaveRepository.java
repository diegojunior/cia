package br.com.totvs.cia.parametrizacao.unidade.importacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;

public interface ParametrizacaoUnidadeImportacaoChaveRepository extends JpaRepository<ParametrizacaoUnidadeImportacaoChave, String>, 
	JpaSpecificationExecutor<ParametrizacaoUnidadeImportacaoChave> {
	
	List<ParametrizacaoUnidadeImportacaoChave> findByCodigo(String codigo);
	
	ParametrizacaoUnidadeImportacaoChave getByCodigo(String codigo);
	
}