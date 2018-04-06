package br.com.totvs.cia.parametrizacao.layout.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutPosicionalJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutPosicionalService;
@Component
public class LayoutPosicionalConverter extends JsonConverter<LayoutPosicional, LayoutPosicionalJson> {

	@Autowired
	private LayoutPosicionalService layoutPosicionalService;
	
	@Autowired
	private SessaoPosicionalConverter sessaoConverter;
	
	@Override
	public LayoutPosicionalJson convertFrom(LayoutPosicional model) {
		LayoutPosicionalJson layoutPosicionalJson = new LayoutPosicionalJson(model);
		for (Sessao sessao : model.getSessoes()) {
			SessaoJson sessaoJson = sessaoConverter.convertFrom(sessao);
			layoutPosicionalJson.addSessao(sessaoJson);
		}
		return layoutPosicionalJson;
	}

	@Override
	public LayoutPosicional convertFrom(LayoutPosicionalJson json) {
		LayoutPosicional layoutPosicional = new LayoutPosicional();
		if (json.getId() != null) {
			layoutPosicional = layoutPosicionalService.findOne(json.getId());
		}
		layoutPosicional.setCodigo(json.getCodigo());
		layoutPosicional.setDescricao(json.getDescricao());
		layoutPosicional.setStatus(StatusLayoutEnum.fromCodigo(json.getStatus().getCodigo()));
		layoutPosicional.setTipoLayout(TipoLayoutEnum.fromCodigo(json.getTipoLayout().getCodigo()));
		layoutPosicional.getSessoes().clear();
		for (SessaoJson sessaoJson : json.getSessoes()) {
			layoutPosicional.addSessao(sessaoConverter.convertFrom(sessaoJson));
		}
		
		return layoutPosicional;
	}

}
