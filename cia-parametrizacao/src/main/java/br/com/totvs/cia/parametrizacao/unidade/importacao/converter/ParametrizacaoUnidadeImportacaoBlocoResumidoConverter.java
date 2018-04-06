package br.com.totvs.cia.parametrizacao.unidade.importacao.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.converters.ConversionException;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.layout.converter.CampoConverter;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ParametrizacaoUnidadeImportacaoBlocoResumidoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoBlocoService;

@Component
public class ParametrizacaoUnidadeImportacaoBlocoResumidoConverter extends JsonConverter<ParametrizacaoUnidadeImportacaoBloco, ParametrizacaoUnidadeImportacaoBlocoResumidoJson>{

	@Autowired
	private CampoConverter campoLayoutConverter;
	
	@Autowired
	private ParametrizacaoUnidadeImportacaoBlocoService service;
	
	@Override
	public ParametrizacaoUnidadeImportacaoBlocoResumidoJson convertFrom(final ParametrizacaoUnidadeImportacaoBloco model) {
		List<CampoJson> camposJson = this.campoLayoutConverter.convertListModelFrom(model.getCamposLayout());
		return new ParametrizacaoUnidadeImportacaoBlocoResumidoJson(model, camposJson);
	}

	@Override
	public ParametrizacaoUnidadeImportacaoBloco convertFrom(final ParametrizacaoUnidadeImportacaoBlocoResumidoJson json) {
		if (json.getId() != null) {
			return this.service.findOne(json.getId());
		}

		throw new ConversionException("Não é possível converter ParametrizacaoUnidadeImportacaoBlocoJson sem o ID.");
	}
}