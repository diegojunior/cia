package br.com.totvs.cia.parametrizacao.unidade.importacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.CampoConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.SessaoDelimitadorConverter;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.LinhaBlocoParametrizacaoUnidadeImportacaoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.LinhaBlocoParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.LinhaBlocoParametrizacaoUnidadeImportacaoService;

@Component
public class LinhaBlocoParametrizacaoUnidadeImportacaoConverter extends JsonConverter<LinhaBlocoParametrizacaoUnidadeImportacao, LinhaBlocoParametrizacaoUnidadeImportacaoJson>{
	
	@Autowired
	private LinhaBlocoParametrizacaoUnidadeImportacaoService linhaBlocoService;
	
	@Autowired
	private SessaoDelimitadorConverter sessaoConverter;
	
	@Autowired
	private CampoConverter campoConverter;
	
	@Override
	public LinhaBlocoParametrizacaoUnidadeImportacaoJson convertFrom(final LinhaBlocoParametrizacaoUnidadeImportacao model) {
		final LinhaBlocoParametrizacaoUnidadeImportacaoJson json = new LinhaBlocoParametrizacaoUnidadeImportacaoJson();
		json.setId(model.getId());
		json.setSessao(this.sessaoConverter.convertFrom(model.getSessao()));
		json.setCampos(this.campoConverter.convertListModelFrom(model.getCampos()));
		return json;
	}

	@Override
	public LinhaBlocoParametrizacaoUnidadeImportacao convertFrom(final LinhaBlocoParametrizacaoUnidadeImportacaoJson json) {
		LinhaBlocoParametrizacaoUnidadeImportacao model = new LinhaBlocoParametrizacaoUnidadeImportacao();
		if (json.getId() != null) {
			model = linhaBlocoService.findOne(json.getId());
		}
		model.setSessao(this.sessaoConverter.convertFrom(json.getSessao()));
		model.setCampos(this.campoConverter.convertListJsonFrom(json.getCampos()));
		return model;
	}
}