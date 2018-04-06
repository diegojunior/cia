package br.com.totvs.cia.conciliacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.conciliacao.json.ConciliacaoResumidaJson;
import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.repository.ConciliacaoRepository;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.converter.PerfilConciliacaoConverter;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.PerfilConciliacaoJson;

@Component
public class ConciliacaoResumidaConverter extends JsonConverter<Conciliacao, ConciliacaoResumidaJson>{
	
	@Autowired
	private ConciliacaoRepository conciliacaoRepository;
	
	@Autowired
	private PerfilConciliacaoConverter perfilConverter;

	@Override
	public ConciliacaoResumidaJson convertFrom(final Conciliacao model) {
		PerfilConciliacaoJson perfilJson = this.perfilConverter.convertFrom(model.getPerfil());
		return new ConciliacaoResumidaJson(model, perfilJson);
	}

	@Override
	public Conciliacao convertFrom(final ConciliacaoResumidaJson json) {
		if (json.getId() != null) {
			return this.conciliacaoRepository.getOne(json.getId());
		}

		throw new ConverterException("Não é possível converter Conciliacao Resumida de Json para Conciliação de Model sem o id");
	}
}