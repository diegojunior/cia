package br.com.totvs.cia.parametrizacao.layout.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutDelimitadorJson;
import br.com.totvs.cia.parametrizacao.layout.json.SessaoJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoDelimitadorEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoDelimitadorEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutDelimitadorService;
@Component
public class LayoutDelimitadorConverter extends JsonConverter<LayoutDelimitador, LayoutDelimitadorJson> {

	@Autowired
	private LayoutDelimitadorService layoutDelimitadorService;
	
	@Autowired
	private SessaoDelimitadorConverter sessaoConverter;
	
	@Override
	public LayoutDelimitadorJson convertFrom(final LayoutDelimitador model) {
		LayoutDelimitadorJson layoutDelimitadorJson = new LayoutDelimitadorJson(model);
		layoutDelimitadorJson.setTipoDelimitador(TipoDelimitadorEnumJson.fromCodigo(model.getTipoDelimitador().getCodigo()));
		for (Sessao sessao : model.getSessoes()) {
			SessaoJson sessaoJson = this.sessaoConverter.convertFrom(sessao);
			layoutDelimitadorJson.addSessao(sessaoJson);
		}
		return layoutDelimitadorJson;
	}

	@Override
	public LayoutDelimitador convertFrom(final LayoutDelimitadorJson json) {
		LayoutDelimitador layoutDelimitador = new LayoutDelimitador();
		if (json.getId() != null) {
			layoutDelimitador = this.layoutDelimitadorService.findOne(json.getId());
		}
		layoutDelimitador.setCodigo(json.getCodigo());
		layoutDelimitador.setDescricao(json.getDescricao());
		layoutDelimitador.setTipoDelimitador(TipoDelimitadorEnum.fromCodigo(json.getTipoDelimitador().getCodigo()));
		layoutDelimitador.setStatus(StatusLayoutEnum.fromCodigo(json.getStatus().getCodigo()));
		layoutDelimitador.setTipoLayout(TipoLayoutEnum.fromCodigo(json.getTipoLayout().getCodigo()));
		layoutDelimitador.getSessoes().clear();
		for (SessaoJson sessaoJson : json.getSessoes()) {
			layoutDelimitador.addSessao(this.sessaoConverter.convertFrom(sessaoJson));
		}
		
		return layoutDelimitador;
	}

}
