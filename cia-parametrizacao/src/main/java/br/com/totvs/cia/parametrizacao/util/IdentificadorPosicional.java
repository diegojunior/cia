package br.com.totvs.cia.parametrizacao.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutPosicionalJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutPosicionalService;

@Component
public class IdentificadorPosicional extends IdentificadorLayout {

	@Autowired
	private LayoutPosicionalService layoutPosicionalService;
	
	public IdentificadorPosicional() {
		this.tipoLayout = TipoLayoutEnumJson.POSICIONAL;
	}
	
	@Override
	protected Layout geraLayout(final String id) {
		return this.layoutPosicionalService.findOne(id);
	}

	@Override
	protected LayoutJson geraLayoutJson() {
		return new LayoutPosicionalJson();
	}
}
