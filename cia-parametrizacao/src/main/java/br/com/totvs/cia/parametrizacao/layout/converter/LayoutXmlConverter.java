package br.com.totvs.cia.parametrizacao.layout.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutXmlJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutXmlService;

@Component
public class LayoutXmlConverter extends JsonConverter<LayoutXml, LayoutXmlJson> {

	@Autowired
	private LayoutXmlService layoutXmlService;

	@Autowired
	private SessaoXmlConverter sessaoConverter;

	@Override
	public LayoutXmlJson convertFrom(LayoutXml model) {
		LayoutXmlJson layoutXmlJson = new LayoutXmlJson(model);
		for (Sessao sessao : model.getSessoes()) {
			SessaoJson sessaoJson = this.sessaoConverter.convertFrom(sessao);
			layoutXmlJson.addSessao(sessaoJson);
		}
		return layoutXmlJson;
	}

	@Override
	public LayoutXml convertFrom(LayoutXmlJson json) {
		LayoutXml layoutXml = new LayoutXml();
		if (json.getId() != null) {
			layoutXml = this.layoutXmlService.findOne(json.getId());
		}
		layoutXml.setCodigo(json.getCodigo());
		layoutXml.setTagRaiz(json.getTagRaiz());
		layoutXml.setDescricao(json.getDescricao());
		layoutXml.setStatus(StatusLayoutEnum.fromCodigo(json.getStatus().getCodigo()));
		layoutXml.setTipoLayout(TipoLayoutEnum.fromCodigo(json.getTipoLayout().getCodigo()));
		layoutXml.getSessoes().clear();
		for (SessaoJson sessaoJson : json.getSessoes()) {
			layoutXml.addSessao(this.sessaoConverter.convertFrom(sessaoJson));
		}
		return layoutXml;
	}
}