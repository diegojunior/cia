package br.com.totvs.cia.cadastro.configuracaoservico.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.configuracaoservico.json.CampoConfiguracaoServicoJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.service.CampoConfiguracaoServicoService;
import br.com.totvs.cia.infra.converter.JsonConverter;

@Component
public class CampoConfiguracaoServicoConverter extends JsonConverter<CampoConfiguracaoServico, CampoConfiguracaoServicoJson> {

	@Autowired
	private CampoConfiguracaoServicoService campoConfiguracaoServicoService;
	
	@Override
	public CampoConfiguracaoServicoJson convertFrom(final CampoConfiguracaoServico model) {
		return new CampoConfiguracaoServicoJson(model);
	}
	
	@Override
	public CampoConfiguracaoServico convertFrom(final CampoConfiguracaoServicoJson json) {
		CampoConfiguracaoServico model = new CampoConfiguracaoServico();
		if (json.getId() != null) {
			model = this.campoConfiguracaoServicoService.findOne(json.getId());
		}
		model.setCampo(json.getCampo());
		model.setLabel(json.getLabel());
		return model;
	}
}
