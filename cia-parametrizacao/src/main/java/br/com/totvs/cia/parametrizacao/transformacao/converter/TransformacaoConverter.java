package br.com.totvs.cia.parametrizacao.transformacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.CampoResumidoConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.LayoutConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.SessaoDefaultConverter;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.transformacao.json.ItemTransformacaoJson;
import br.com.totvs.cia.parametrizacao.transformacao.json.TransformacaoJson;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacao;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;

@Component
public class TransformacaoConverter extends JsonConverter<Transformacao, TransformacaoJson>{
	
	@Autowired
	private LayoutConverter layoutConverter;
	
	@Autowired
	private SessaoDefaultConverter sessaoConverter;
	
	@Autowired
	private CampoResumidoConverter campoConverter;
	
	@Autowired
	private ItemTransformacaoConverter itemTransformacaoConverter;

	@Override
	public Transformacao convertFrom(final TransformacaoJson json) {
		
		Layout layout = this.layoutConverter.convertFrom(json.getLayout());
		
		Sessao sessao = this.sessaoConverter.convertFrom(json.getSessao());
		
		Campo campo = this.campoConverter.convertFrom(json.getCampo());
		
		ItemTransformacao item = this.itemTransformacaoConverter.convertFrom(json.getItem());
		
		return new Transformacao(json, layout, sessao, campo, item);
	}

	@Override
	public TransformacaoJson convertFrom(final Transformacao model) {
		
		LayoutJson layout = this.layoutConverter.convertFrom(model.getLayout());
		
		SessaoJson sessao = this.sessaoConverter.convertFrom(model.getSessao());
		
		CampoJson campo = this.campoConverter.convertFrom(model.getCampo());
		
		ItemTransformacaoJson item = this.itemTransformacaoConverter.convertFrom(model.getItem());
		
		return new TransformacaoJson(model, layout, sessao, campo, item);
	}
}