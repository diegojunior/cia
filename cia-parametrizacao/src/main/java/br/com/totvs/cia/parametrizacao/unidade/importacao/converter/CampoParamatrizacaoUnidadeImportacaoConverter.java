package br.com.totvs.cia.parametrizacao.unidade.importacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.CampoConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.SessaoDefaultConverter;
import br.com.totvs.cia.parametrizacao.layout.service.CampoService;
import br.com.totvs.cia.parametrizacao.layout.service.SessaoService;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.CampoParametrizacaoUnidadeImportacaoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.CampoParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.CampoParametrizacaoUnidadeImportacaoService;

@Component
public class CampoParamatrizacaoUnidadeImportacaoConverter extends JsonConverter<CampoParametrizacaoUnidadeImportacao, CampoParametrizacaoUnidadeImportacaoJson> {

	@Autowired
	private CampoConverter campoConverter;
	
	@Autowired
	private SessaoDefaultConverter sessaoConverter;
	
	@Autowired
	private CampoParametrizacaoUnidadeImportacaoService campoUnidadeImportacaoService;
	
	@Autowired
	private SessaoService sessaoService;
	
	@Autowired
	private CampoService campoService;
	
	@Override
	public CampoParametrizacaoUnidadeImportacao convertFrom(CampoParametrizacaoUnidadeImportacaoJson json) {
		CampoParametrizacaoUnidadeImportacao campo = new CampoParametrizacaoUnidadeImportacao();
		if (json.getId() != null) {
			campo = this.campoUnidadeImportacaoService.findOne(json.getId());	
		}
		campo.setSessao(this.sessaoService.findOne(json.getSessao().getId()));
		campo.setCampo(this.campoService.findOne(json.getCampo().getId()));
		return campo;
	}
	
	@Override
	public CampoParametrizacaoUnidadeImportacaoJson convertFrom(CampoParametrizacaoUnidadeImportacao model) {
		return new CampoParametrizacaoUnidadeImportacaoJson(model.getId(), this.sessaoConverter.convertFrom(model.getSessao()), 
				this.campoConverter.convertFrom(model.getCampo()));
	}
}