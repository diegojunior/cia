package br.com.totvs.cia.parametrizacao.validacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.infra.exception.CiaBusinessException;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.AbstractValidacaoArquivo;
import br.com.totvs.cia.parametrizacao.validacao.model.CampoValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.LocalValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.TipoValidacaoEnum;
import br.com.totvs.cia.parametrizacao.validacao.repository.ValidacaoArquivoRepository;
import br.com.totvs.cia.parametrizacao.validacao.repository.ValidacaoArquivoSpecification;

@Service
public class ValidacaoArquivoService {

	@Autowired
	private ValidacaoArquivoRepository validacaoRepository;
	
	public List<AbstractValidacaoArquivo> search(final TipoLayoutEnum tipoLayout, final String layout, 
			final TipoValidacaoEnum tipoValidacao, final CampoValidacaoArquivoEnum campoValidacao, final LocalValidacaoArquivoEnum localValidacao) {
		return this.validacaoRepository.findAll(ValidacaoArquivoSpecification.findBy(tipoLayout, layout, tipoValidacao, campoValidacao, localValidacao));
	}
	
	public List<AbstractValidacaoArquivo> listBy(final TipoLayoutEnum tipoLayout, final Layout layout) {
		return this.validacaoRepository.findAll(ValidacaoArquivoSpecification.findBy(tipoLayout, layout));
	}

	public AbstractValidacaoArquivo incluir(final AbstractValidacaoArquivo model) {
		
		final AbstractValidacaoArquivo validacaoArquivoExistente = this.validacaoRepository.findByCampoValidacaoAndLayout(model.getCampoValidacao(), model.getLayout());
		
		final Boolean isValidacaoExistente = validacaoArquivoExistente != null;
		if(isValidacaoExistente) {
			throw new CiaBusinessException("Chave(s) Duplicada(s).");
		}
		
		return this.validacaoRepository.save(model);
	}
	
	public AbstractValidacaoArquivo alterar(final AbstractValidacaoArquivo model) {
		return this.validacaoRepository.save(model);
	}

	public void delete(final Iterable<AbstractValidacaoArquivo> entities) {
		this.validacaoRepository.delete(entities);
	}

	public void deleteAll() {
		this.validacaoRepository.deleteAll();
	}
}