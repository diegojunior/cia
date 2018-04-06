package br.com.totvs.cia.cadastro.configuracaoservico.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.json.CampoConfiguracaoServicoJson;
import br.com.totvs.cia.cadastro.configuracaoservico.json.ConfiguracaoServicoJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.CamposConfiguracoesServicos;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.TipoServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.service.ConfiguracaoServicoService;
import br.com.totvs.cia.infra.converter.JsonConverter;

@Component
public class ConfiguracaoServicoConverter extends JsonConverter<ConfiguracaoServico, ConfiguracaoServicoJson> {

	@Autowired
	private ConfiguracaoServicoService configuracaoServicoService;
	
	@Autowired
	private CampoConfiguracaoServicoConverter campoConfiguracaoServicoConverter;
	
	@Override
	public ConfiguracaoServicoJson convertFrom(final ConfiguracaoServico model) {
		
		List<CampoConfiguracaoServicoJson> camposJson = this.campoConfiguracaoServicoConverter.convertListModelFrom(model.getCampos());
		
		return new ConfiguracaoServicoJson(model, camposJson);
	}
	
	@Override
	public ConfiguracaoServico convertFrom(final ConfiguracaoServicoJson json) {
		ConfiguracaoServico model = new ConfiguracaoServico();
		if (json.getId() != null) {
			model = this.configuracaoServicoService.findOne(json.getId());
		}
		
		List<CampoConfiguracaoServico> camposModel = this.campoConfiguracaoServicoConverter.convertListJsonFrom(json.getCampos());
		
		model.setCodigo(json.getCodigo());
		model.setDescricao(json.getDescricao());
		model.setServico(ServicoEnum.fromCodigo(json.getServico().getCodigo()));
		model.setTipoServico(TipoServicoEnum.fromCodigo(json.getTipoServico().getCodigo()));
		model.setSistema(SistemaEnum.fromCodigo(json.getSistema().getCodigo()));

		model.atualizarCampos(CamposConfiguracoesServicos.build(model, camposModel));

		return model;
	}
}
