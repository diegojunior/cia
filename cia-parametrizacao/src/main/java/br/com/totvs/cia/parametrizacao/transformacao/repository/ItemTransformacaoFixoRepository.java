package br.com.totvs.cia.parametrizacao.transformacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixo;

public interface ItemTransformacaoFixoRepository extends JpaRepository<ItemTransformacaoFixo, String>, JpaSpecificationExecutor<ItemTransformacaoFixo>{
	
}