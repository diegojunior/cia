package br.com.totvs.cia.parametrizacao.perfilconciliacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.configuracaoservico.converter.CampoConfiguracaoServicoConverter;
import br.com.totvs.cia.cadastro.configuracaoservico.json.CampoConfiguracaoServicoJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.RegraPerfilJson;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.ConfiguracaoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.Regra;

@Component
public class RegraPerfilConciliacaoConverter extends JsonConverter<Regra, RegraPerfilJson> {
	
	@Autowired
	private CampoConfiguracaoServicoConverter campoCargaConverter;
	
	private ConfiguracaoPerfilConciliacao configuracao;
	
	public void of (final ConfiguracaoPerfilConciliacao configuracao) {
		this.configuracao = configuracao;
	}
	
	@Override
	public Regra convertFrom(final RegraPerfilJson json) {
		CampoConfiguracaoServico campoCarga = null;
		Campo campoImportacao = null;
		if (json.getModulo().isCarga()) {
			campoCarga = this.campoCargaConverter.convertFrom(json.getCampoCarga());
		}
		if (json.getModulo().isImportacao()) {
			campoImportacao = this.configuracao.getCampoBy(json.getCampoImportacao());
		}
		return new Regra(json, campoCarga, campoImportacao);
	}
	
	@Override
	public RegraPerfilJson convertFrom(final Regra model) {
		CampoConfiguracaoServicoJson campoCarga = null;
		String campoImportacao = null;
		if (model.getModulo().isCarga()) {
			campoCarga = this.campoCargaConverter.convertFrom(model.getCampoCarga());
		}
		if (model.getModulo().isImportacao()) {
			campoImportacao = model.getCampoImportacao().getDominio().getCodigo();
		}
		return new RegraPerfilJson(model, campoCarga, campoImportacao);
	}
}