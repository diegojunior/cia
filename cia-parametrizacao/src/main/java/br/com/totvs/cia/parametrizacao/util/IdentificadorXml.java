package br.com.totvs.cia.parametrizacao.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutXmlJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutXmlService;
@Component
public class IdentificadorXml extends IdentificadorLayout {

	@Autowired
	private LayoutXmlService layoutXmlService;
	
	public IdentificadorXml() {
		this.tipoLayout = TipoLayoutEnumJson.XML;
	}
	
	@Override
	protected Layout geraLayout(final String id) {
		return this.layoutXmlService.findOne(id);
	}

	@Override
	protected LayoutJson geraLayoutJson() {
		return new LayoutXmlJson();
	}
}
