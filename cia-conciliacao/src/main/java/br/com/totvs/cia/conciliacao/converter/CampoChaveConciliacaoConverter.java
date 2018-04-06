package br.com.totvs.cia.conciliacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.conciliacao.json.CampoChaveConciliacaoJson;
import br.com.totvs.cia.conciliacao.model.CampoChaveConciliacao;
import br.com.totvs.cia.conciliacao.repository.CampoChaveConciliacaoRepository;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;

@Component
public class CampoChaveConciliacaoConverter extends JsonConverter<CampoChaveConciliacao, CampoChaveConciliacaoJson>{
	
	@Autowired
	private CampoChaveConciliacaoRepository chaveRepository;

	@Override
	public CampoChaveConciliacaoJson convertFrom(final CampoChaveConciliacao model) {
		return new CampoChaveConciliacaoJson(model);
	}

	@Override
	public CampoChaveConciliacao convertFrom(final CampoChaveConciliacaoJson json) {
		if (json.getId() != null) {
			return this.chaveRepository.findOne(json.getId());
		}
		throw new ConverterException("Não é possível converter Campo Chave de Conciliacao Json para Campo Chave de Conciliação de Model sem o id");
	}
}