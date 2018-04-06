package br.com.totvs.cia.parametrizacao.perfilconciliacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.configuracaoservico.converter.CampoConfiguracaoServicoConverter;
import br.com.totvs.cia.cadastro.configuracaoservico.json.CampoConfiguracaoServicoJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.service.CampoConfiguracaoServicoService;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.ConfiguracaoCampoJson;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.LocalizacaoCamposJsonEnum;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.CampoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.AbstractParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;

@Component
public class CampoPerfilConciliacaoConverter extends JsonConverter<CampoPerfilConciliacao, ConfiguracaoCampoJson> {
	
	@Autowired
	private CampoConfiguracaoServicoService campoServicoService;
	
	@Autowired
	private CampoConfiguracaoServicoConverter campoConfiguracaoServicoConverter;
	
	private Layout layout;
	
	private String codigoSessao;
	
	private ConfiguracaoServico servico;
	
	private AbstractParametrizacaoUnidadeImportacao unidade;
	
	private LocalizacaoCamposJsonEnum localizacaoCampos;
	
	public void of(final ConfiguracaoServico servico, final Layout layout, 
			final String codigoSessao, final LocalizacaoCamposJsonEnum localizacaoCampos) {
		this.servico = servico;
		this.layout = layout;
		this.codigoSessao = codigoSessao;
		this.localizacaoCampos = localizacaoCampos;
	}
	
	public void of(final ConfiguracaoServico servico, final AbstractParametrizacaoUnidadeImportacao unidade, 
			final LocalizacaoCamposJsonEnum localizacaoCampos) {
		this.servico = servico;
		this.unidade = unidade;
		this.localizacaoCampos = localizacaoCampos;
	}
	
	@Override
	public CampoPerfilConciliacao convertFrom(final ConfiguracaoCampoJson json) {
		CampoConfiguracaoServico campoCarga = null;
		CampoConfiguracaoServico campoEquivalente = null;
		Dominio campoImportacao = null;
		if (json.getCampoCarga() != null) {
			campoCarga = this.campoServicoService.getBy(json.getCampoCarga().getCampo(), this.servico);
		}
		if (json.getCampoEquivalente() != null) {
			campoEquivalente = this.campoServicoService.getBy(json.getCampoEquivalente().getCampo(), this.servico);
		}
		if (json.getCampoImportacao() != null && !"".equals(json.getCampoImportacao())) {
			if (this.localizacaoCampos.isSessao()) {
				Campo campoImportacaoEncontrado = this.layout.getCampoSessaoBy(this.codigoSessao, json.getCampoImportacao());
				campoImportacao = campoImportacaoEncontrado != null ? campoImportacaoEncontrado.getDominio() : null;
			}
			if (this.localizacaoCampos.isUnidade()) {
				if (this.unidade.getTipoLayout().isDelimitador()) {
					Campo campoImportacaoEncontrado = ((ParametrizacaoUnidadeImportacaoBloco)this.unidade).getCampoBy(json.getCampoImportacao());
					campoImportacao = campoImportacaoEncontrado != null ? campoImportacaoEncontrado.getDominio() : null;
				} else {
					Campo campoImportacaoEncontrado = ((ParametrizacaoUnidadeImportacaoChave)this.unidade).getCampoBy(json.getCampoImportacao());
					campoImportacao = campoImportacaoEncontrado != null ? campoImportacaoEncontrado.getDominio() : null;
				}
			}
		}
		return new CampoPerfilConciliacao(json, campoCarga, campoImportacao, campoEquivalente);
	}

	@Override
	public ConfiguracaoCampoJson convertFrom(final CampoPerfilConciliacao model) {
		CampoConfiguracaoServicoJson campoCargaJson = null;
		CampoConfiguracaoServicoJson campoEquivalenteJson = null;
		if (model.getCampoCarga() != null) {
			campoCargaJson = this.campoConfiguracaoServicoConverter.convertFrom(model.getCampoCarga());
		}
		if (model.getCampoEquivalente() != null) {
			campoEquivalenteJson = this.campoConfiguracaoServicoConverter.convertFrom(model.getCampoEquivalente());
		}
		return new ConfiguracaoCampoJson(model, campoCargaJson, campoEquivalenteJson);
	}
}