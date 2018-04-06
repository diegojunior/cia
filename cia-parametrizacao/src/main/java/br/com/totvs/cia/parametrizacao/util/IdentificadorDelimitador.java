package br.com.totvs.cia.parametrizacao.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.parametrizacao.layout.json.LayoutDelimitadorJson;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutDelimitadorService;

@Component
public class IdentificadorDelimitador extends IdentificadorLayout {

	@Autowired
	private LayoutDelimitadorService layoutDelimitadorService;
	
	public IdentificadorDelimitador() {
		this.tipoLayout = TipoLayoutEnumJson.DELIMITADOR;
	}
	
	@Override
	protected Layout geraLayout(final String id) {
		return this.layoutDelimitadorService.findOne(id);
	}

	@Override
	protected LayoutJson geraLayoutJson() {
		return new LayoutDelimitadorJson();
	}
}
