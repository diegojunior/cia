package br.com.totvs.cia.parametrizacao.perfilconciliacao.converter;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.service.ConfiguracaoServicoService;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.ConfiguracaoCampoJson;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.ConfiguracaoPerfilJson;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.CampoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.ConfiguracaoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.AbstractParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoService;

@Component
public class ConfiguracaoPerfilConciliacaoConverter extends JsonConverter<ConfiguracaoPerfilConciliacao, ConfiguracaoPerfilJson> {
	
	@Autowired
	private ParametrizacaoUnidadeImportacaoService unidadeService;
	
	@Autowired
	private ConfiguracaoServicoService servicoService;
	
	@Autowired
	private CampoPerfilConciliacaoConverter campoPerfilConverter;
	
	private Layout layout;
	
	public void of(final Layout layout) {
		this.layout = layout;
	}
	
	@Override
	public ConfiguracaoPerfilConciliacao convertFrom(final ConfiguracaoPerfilJson json) {
		ConfiguracaoServico servico = this.servicoService.getBy(json.getServico());
		
		if (json.getLocalizacaoCampos().isSessao()) {
			this.campoPerfilConverter.of(servico, this.layout, json.getIdentificacao(), json.getLocalizacaoCampos());
			List<CampoPerfilConciliacao> campos = this.campoPerfilConverter.convertListJsonFrom(json.getConfiguracoesCampos());
			
			Sessao sessao = this.layout.getBy(json.getIdentificacao());
			
			return new ConfiguracaoPerfilConciliacao(sessao, servico, campos, json.isConsolidarDados());
		}
		if (json.getLocalizacaoCampos().isUnidade()) {
			AbstractParametrizacaoUnidadeImportacao unidade = this.unidadeService.getBy(json.getIdentificacao());
			this.campoPerfilConverter.of(servico, unidade, json.getLocalizacaoCampos());
			
			List<CampoPerfilConciliacao> campos = this.campoPerfilConverter.convertListJsonFrom(json.getConfiguracoesCampos());
			
			return new ConfiguracaoPerfilConciliacao(unidade, servico, campos, json.isConsolidarDados());
		}
		throw new ConverterException("Não foi possível identificar a localização dos campos");
	}
	
	@Override
	public ConfiguracaoPerfilJson convertFrom(final ConfiguracaoPerfilConciliacao model) {
		List<ConfiguracaoCampoJson> configuracoesCampos = this.campoPerfilConverter.convertListModelFrom(model.getCampos());
		Collections.sort(configuracoesCampos);
		return new ConfiguracaoPerfilJson (model, configuracoesCampos);
	}
}