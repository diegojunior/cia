package br.com.totvs.cia.parametrizacao.unidade.importacao.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.converters.ConversionException;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.CampoConverter;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ParametrizacaoUnidadeImportacaoChaveResumidoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoChaveService;

@Component
public class ParametrizacaoUnidadeImportacaoChaveResumidoConverter extends JsonConverter<ParametrizacaoUnidadeImportacaoChave, ParametrizacaoUnidadeImportacaoChaveResumidoJson>{

	@Autowired
	private CampoConverter campoLayoutConverter;
	
	@Autowired
	private ParametrizacaoUnidadeImportacaoChaveService service;
	
	@Override
	public ParametrizacaoUnidadeImportacaoChaveResumidoJson convertFrom(ParametrizacaoUnidadeImportacaoChave model) {
		List<CampoJson> camposJson = this.campoLayoutConverter.convertListModelFrom(model.getCamposLayout());
		return new ParametrizacaoUnidadeImportacaoChaveResumidoJson(model, camposJson);
		
	}

	@Override
	public ParametrizacaoUnidadeImportacaoChave convertFrom(ParametrizacaoUnidadeImportacaoChaveResumidoJson json) {
		if (json.getId() != null) {
			return this.service.getOne(json.getId());
		}

		throw new ConversionException("Não é possível converter ParametrizacaoUnidadeImportacaoJson sem o ID.");
	}

}
