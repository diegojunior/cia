package br.com.totvs.cia.parametrizacao.unidade.importacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.converters.ConversionException;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.CampoUnidadeImportacaoResumidoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.CampoParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.CampoParametrizacaoUnidadeImportacaoService;

@Component
public class CampoParametrizacaoUnidadeImportacaoResumidoConverter extends JsonConverter<CampoParametrizacaoUnidadeImportacao, CampoUnidadeImportacaoResumidoJson> {
	
	@Autowired
	private CampoParametrizacaoUnidadeImportacaoService campoUnidadeImportacaoService;
	
	@Override
	public CampoUnidadeImportacaoResumidoJson convertFrom(CampoParametrizacaoUnidadeImportacao model) {
		return new CampoUnidadeImportacaoResumidoJson(model.getId(), model.getCampo().getDominio().getCodigo());
	}

	@Override
	public CampoParametrizacaoUnidadeImportacao convertFrom(CampoUnidadeImportacaoResumidoJson json) {
		if (json.getId() != null) {
			return campoUnidadeImportacaoService.findOne(json.getId());	
		}
		throw new ConversionException("Não é possível converter CampoUnidadeImportacaoJson sem o ID.");
	}

}
