package br.com.totvs.cia.parametrizacao.validacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.CampoConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.LayoutConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.SessaoDefaultConverter;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.validacao.json.ValidacaoArquivoJson;
import br.com.totvs.cia.parametrizacao.validacao.model.AbstractValidacaoArquivo;
import br.com.totvs.cia.parametrizacao.validacao.model.ValidacaoArquivoExterno;
import br.com.totvs.cia.parametrizacao.validacao.model.ValidacaoArquivoInterno;

@Component
public class ValidacaoArquivoConverter extends JsonConverter<AbstractValidacaoArquivo, ValidacaoArquivoJson> {
	
	@Autowired
	private LayoutConverter layoutConverter;
	
	@Autowired
	private SessaoDefaultConverter sessaoConverter;
	
	@Autowired
	private CampoConverter campoConverter;
	
	@Override
	public AbstractValidacaoArquivo convertFrom(final ValidacaoArquivoJson json) {
		
		Layout layoutModel = this.layoutConverter.convertFrom(json.getLayout());
		
		if (json.getLocalValidacao().isExterno()) {
			return new ValidacaoArquivoExterno(json, layoutModel);
		}
		
		Sessao sessaoModel = this.sessaoConverter.convertFrom(json.getSessaoLayout());
		Campo campoModel = this.campoConverter.convertFrom(json.getCampoLayout());
		
		return new ValidacaoArquivoInterno(json, layoutModel, sessaoModel, campoModel);
	}

	@Override
	public ValidacaoArquivoJson convertFrom(final AbstractValidacaoArquivo model) {
		
		LayoutJson layoutJson = this.layoutConverter.convertFrom(model.getLayout());
		
		if (model.getLocalValidacao().isExterno()) {
			ValidacaoArquivoExterno externo = (ValidacaoArquivoExterno) model;
			return new ValidacaoArquivoJson(externo, layoutJson);
		}
		
		ValidacaoArquivoInterno interno = (ValidacaoArquivoInterno) model;
		
		SessaoJson sessaoJson = this.sessaoConverter.convertFrom(interno.getSessaoLayout());
		CampoJson campoJson = this.campoConverter.convertFrom(interno.getCampoLayout());
		
		return new ValidacaoArquivoJson(interno, layoutJson, sessaoJson, campoJson);
		
	}
}