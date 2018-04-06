package br.com.totvs.cia.conciliacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.conciliacao.json.CampoInformativoJson;
import br.com.totvs.cia.conciliacao.model.CampoInformativo;
import br.com.totvs.cia.conciliacao.repository.CampoInformativoRepository;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;

@Component
public class CampoInformativoConverter extends JsonConverter<CampoInformativo, CampoInformativoJson>{
	
	@Autowired
	private CampoInformativoRepository informativoRepository;

	@Override
	public CampoInformativoJson convertFrom(final CampoInformativo model) {
		return new CampoInformativoJson(model);
	}

	@Override
	public CampoInformativo convertFrom(final CampoInformativoJson json) {
		if (json.getId() != null) {
			return this.informativoRepository.findOne(json.getId());
		}
		throw new ConverterException("Não é possível converter Campo Informativo Json para Campo Informativo Model sem o id");
	}
}