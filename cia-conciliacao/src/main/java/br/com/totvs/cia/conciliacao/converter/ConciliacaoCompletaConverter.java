package br.com.totvs.cia.conciliacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.conciliacao.json.ConciliacaoCompletaJson;
import br.com.totvs.cia.conciliacao.json.LoteConciliacaoJson;
import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.repository.ConciliacaoRepository;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.converter.PerfilConciliacaoConverter;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.PerfilConciliacaoJson;

@Component
public class ConciliacaoCompletaConverter extends JsonConverter<Conciliacao, ConciliacaoCompletaJson>{
	
	@Autowired
	private ConciliacaoRepository conciliacaoRepository;
	
	@Autowired
	private PerfilConciliacaoConverter perfilConverter;

	@Autowired
	private LoteConciliacaoConverter loteConciliacaoConverter;

	@Override
	public ConciliacaoCompletaJson convertFrom(final Conciliacao model) {
		PerfilConciliacaoJson perfilJson = this.perfilConverter.convertFrom(model.getPerfil());
		LoteConciliacaoJson loteJson = this.loteConciliacaoConverter.convertFrom(model.getLote());
		return new ConciliacaoCompletaJson(model, perfilJson, loteJson);
	}

	@Override
	public Conciliacao convertFrom(final ConciliacaoCompletaJson json) {
		if (json.getId() != null) {
			return this.conciliacaoRepository.findOne(json.getId());
		}
		throw new ConverterException("Não é possível converter Conciliacao Json para Conciliação Model sem o id");
	}
}