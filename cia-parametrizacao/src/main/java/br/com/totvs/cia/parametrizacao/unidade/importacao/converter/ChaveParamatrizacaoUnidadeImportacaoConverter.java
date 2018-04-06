package br.com.totvs.cia.parametrizacao.unidade.importacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.CampoConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.SessaoDefaultConverter;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ChaveParametrizacaoUnidadeImportacaoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ChaveParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ChaveParametrizacaoUnidadeImportacaoService;

@Component
public class ChaveParamatrizacaoUnidadeImportacaoConverter extends JsonConverter<ChaveParametrizacaoUnidadeImportacao, ChaveParametrizacaoUnidadeImportacaoJson> {

	@Autowired
	private CampoConverter campoConverter;
	
	@Autowired
	private SessaoDefaultConverter sessaoConverter;
	
	@Autowired
	private ChaveParametrizacaoUnidadeImportacaoService chaveUnidadeImportacaoService;
	
	@Override
	public ChaveParametrizacaoUnidadeImportacaoJson convertFrom(final ChaveParametrizacaoUnidadeImportacao model) {
		return new ChaveParametrizacaoUnidadeImportacaoJson(model.getId(), this.campoConverter.convertFrom(model.getCampo()), this.sessaoConverter.convertListModelFrom(model.getSessoes()));
	}

	@Override
	public ChaveParametrizacaoUnidadeImportacao convertFrom(final ChaveParametrizacaoUnidadeImportacaoJson json) {
		ChaveParametrizacaoUnidadeImportacao chave = new ChaveParametrizacaoUnidadeImportacao();
		if (json.getId() != null) {
			chave = this.chaveUnidadeImportacaoService.findOne(json.getId());	
		}
		
		chave.setSessoes(this.sessaoConverter.convertListJsonFrom(json.getSessoes()));
		chave.setCampo(this.campoConverter.convertFrom(json.getCampo()));
		return chave;
	}

}
