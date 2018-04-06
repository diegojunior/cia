package br.com.totvs.cia.conciliacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.conciliacao.json.CampoConciliacaoJson;
import br.com.totvs.cia.conciliacao.model.CampoConciliacao;
import br.com.totvs.cia.conciliacao.repository.CampoConciliacaoRepository;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;

@Component
public class CampoConciliacaoConverter extends JsonConverter<CampoConciliacao, CampoConciliacaoJson>{
	
	@Autowired
	private CampoConciliacaoRepository campoConciliacaoRepository;

	@Override
	public CampoConciliacaoJson convertFrom(final CampoConciliacao model) {
		return new CampoConciliacaoJson(model);
	}

	@Override
	public CampoConciliacao convertFrom(final CampoConciliacaoJson json) {
		if (json.getId() != null) {
			return this.campoConciliacaoRepository.findOne(json.getId());
		}

		throw new ConverterException("Não é possível converter Campo de Conciliacao Json para Campo de Conciliação de Model sem o id");
	}
}