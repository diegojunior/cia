package br.com.totvs.cia.cadastro.configuracaoservico.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.configuracaoservico.json.ConfiguracaoServicoJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.service.ConfiguracaoServicoService;
import br.com.totvs.cia.infra.converter.JsonConverter;

@Component
public class ConfiguracaoServicoResumidoConverter extends JsonConverter<ConfiguracaoServico, ConfiguracaoServicoJson> {

	@Autowired
	private ConfiguracaoServicoService configuracaoServicoService;
	
	@Override
	public ConfiguracaoServicoJson convertFrom(final ConfiguracaoServico model) {
		return new ConfiguracaoServicoJson(model);
	}
	
	@Override
	public ConfiguracaoServico convertFrom(final ConfiguracaoServicoJson json) {
		if (json.getId() != null) {
			return this.configuracaoServicoService.findOne(json.getId());
		}
		return new ConfiguracaoServico(json);
	}
}
