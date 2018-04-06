package br.com.totvs.cia.parametrizacao.layout.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.thoughtworks.xstream.converters.ConversionException;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.dominio.converter.DominioConverter;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.service.CampoService;

@Component
public class CampoResumidoConverter extends JsonConverter<Campo, CampoJson> {

	@Autowired
	private DominioConverter dominioConverter;

	@Autowired
	private CampoService campoService;

	@Override
	public CampoJson convertFrom(final Campo model) {
		
		return new CampoJson(model.getId(), model.getOrdem(), this.dominioConverter.convertFrom(model.getDominio()), model.getDescricao(),
				model.getTag(), model.getTamanho(), model.getPosicaoInicial(), model.getPosicaoFinal(),
				model.getPattern());
	}

	@Override
	public Campo convertFrom(final CampoJson json) {
		if (json.getId() != null
				&& !"".equals(json.getId())) {
			return this.campoService.findOne(json.getId());
		}
		throw new ConversionException("Não é possível converter Campo sem o ID.");
	}

}
