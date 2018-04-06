package br.com.totvs.cia.parametrizacao.layout.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.util.IdentificadorDelimitador;
import br.com.totvs.cia.parametrizacao.util.IdentificadorPosicional;
import br.com.totvs.cia.parametrizacao.util.IdentificadorXml;
@Component
public class LayoutConverter extends JsonConverter<Layout, LayoutJson>{

	@Autowired
	private IdentificadorPosicional identificadorPosicional;
	
	@Autowired
	private IdentificadorDelimitador identificadorDelimitador;
	
	@Autowired
	private IdentificadorXml identificadorXml;
	
	@Autowired
	private SessaoDelimitadorConverter sessaoDelimitadorConverter;
	
	@Autowired
	private SessaoPosicionalConverter sessaoPosicionalConverter;
	
	@Autowired
	private SessaoXmlConverter sessaoXmlConverter;
	
	private Layout identificaLayout(final LayoutJson layoutJson) {
		this.identificadorPosicional.setSucessor(this.identificadorXml);
		this.identificadorXml.setSucessor(this.identificadorDelimitador);
		return this.identificadorPosicional.processaRequest(layoutJson);
	}
	
	private LayoutJson identificaLayoutJson(final Layout layout) {
		this.identificadorPosicional.setSucessor(this.identificadorXml);
		this.identificadorXml.setSucessor(this.identificadorDelimitador);
		return this.identificadorPosicional.processaRequestJson(layout);
	}
	
	
	@Override
	public LayoutJson convertFrom(final Layout model) {
		LayoutJson layoutJson = this.identificaLayoutJson(model);
		layoutJson.setId(model.getId());
		layoutJson.setCodigo(model.getCodigo());
		layoutJson.setTipoLayout(TipoLayoutEnumJson.fromCodigo(model.getTipoLayout().getCodigo()));
		for (Sessao sessao : model.getSessoes()) {
			if (model.getTipoLayout().isDelimitador()) {
				layoutJson.getSessoes().add(this.sessaoDelimitadorConverter.convertFrom(sessao));
			}
			if (model.getTipoLayout().isPosicional()) {
				layoutJson.getSessoes().add(this.sessaoPosicionalConverter.convertFrom(sessao));
			}
			if (model.getTipoLayout().isXml()) {
				layoutJson.getSessoes().add(this.sessaoXmlConverter.convertFrom(sessao));
			}
		}
		
		return layoutJson;
	}

	@Override
	public Layout convertFrom(final LayoutJson json) {

		return this.identificaLayout(json);
		
	}

}
