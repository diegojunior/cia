package br.com.totvs.cia.conciliacao.converter;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.conciliacao.json.LoteConciliacaoJson;
import br.com.totvs.cia.conciliacao.json.UnidadeConciliacaoJson;
import br.com.totvs.cia.conciliacao.model.LoteConciliacao;
import br.com.totvs.cia.conciliacao.repository.LoteConciliacaoRepository;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.converter.ConfiguracaoPerfilConciliacaoConverter;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.ConfiguracaoPerfilJson;

@Component
public class LoteConciliacaoConverter extends JsonConverter<LoteConciliacao, LoteConciliacaoJson>{
	
	@Autowired
	private LoteConciliacaoRepository loteConciliacaoRepository;
	
	@Autowired
	private ConfiguracaoPerfilConciliacaoConverter configuracaoPerfilConverter;
	
	@Autowired
	private UnidadeConciliacaoConverter unidadeConciliacaoConverter;

	@Override
	public LoteConciliacaoJson convertFrom(final LoteConciliacao model) {
		ConfiguracaoPerfilJson configuracaoPerfilJson = this.configuracaoPerfilConverter.convertFrom(model.getConciliacao().getPerfil().getConfiguracao());
		List<UnidadeConciliacaoJson> unidadesJson = this.unidadeConciliacaoConverter.convertListModelFrom(model.getUnidades());
		Collections.sort(unidadesJson);
		return new LoteConciliacaoJson(model, configuracaoPerfilJson, unidadesJson);
	}

	@Override
	public LoteConciliacao convertFrom(final LoteConciliacaoJson json) {
		if (json.getId() != null) {
			return this.loteConciliacaoRepository.findOne(json.getId());
		}
		throw new ConverterException("Não é possível converter Lote de Conciliacao Json para Lote de Conciliação de Model sem o id");
	}
}