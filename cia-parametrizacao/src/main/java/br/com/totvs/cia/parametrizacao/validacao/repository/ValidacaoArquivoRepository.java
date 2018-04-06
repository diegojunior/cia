package br.com.totvs.cia.parametrizacao.validacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.validacao.model.AbstractValidacaoArquivo;
import br.com.totvs.cia.parametrizacao.validacao.model.CampoValidacaoArquivoEnum;

public interface ValidacaoArquivoRepository extends JpaRepository<AbstractValidacaoArquivo, String>, JpaSpecificationExecutor<AbstractValidacaoArquivo>{
	
	AbstractValidacaoArquivo findByCampoValidacao(final CampoValidacaoArquivoEnum campoValidacao);
	
	AbstractValidacaoArquivo findByCampoValidacaoAndLayout(final CampoValidacaoArquivoEnum campoValidacao, final Layout layout);
	
}