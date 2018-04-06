package br.com.totvs.cia.parametrizacao.unidade.importacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.converters.ConversionException;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ParametrizacaoUnidadeImportacaoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.AbstractParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoService;

@Component
public class ParametrizacaoUnidadeImportacaoConverter extends JsonConverter<AbstractParametrizacaoUnidadeImportacao, ParametrizacaoUnidadeImportacaoJson>{

	@Autowired
	private ParametrizacaoUnidadeImportacaoService service;
	
	@Override
	public ParametrizacaoUnidadeImportacaoJson convertFrom(final AbstractParametrizacaoUnidadeImportacao model) {
		return new ParametrizacaoUnidadeImportacaoJson(model);
	}

	@Override
	public AbstractParametrizacaoUnidadeImportacao convertFrom(ParametrizacaoUnidadeImportacaoJson json) {
		if (json.getId() != null) {
			return this.service.getOne(json.getId());
		}
		throw new ConversionException("Não é possível converter ParametrizacaoUnidadeImportacaoJson sem o ID.");
	}
}