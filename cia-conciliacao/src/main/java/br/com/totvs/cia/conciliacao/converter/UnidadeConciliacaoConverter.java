package br.com.totvs.cia.conciliacao.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.conciliacao.json.CampoChaveConciliacaoJson;
import br.com.totvs.cia.conciliacao.json.CampoConciliacaoJson;
import br.com.totvs.cia.conciliacao.json.CampoInformativoJson;
import br.com.totvs.cia.conciliacao.json.UnidadeConciliacaoJson;
import br.com.totvs.cia.conciliacao.model.UnidadeConciliacao;
import br.com.totvs.cia.conciliacao.repository.UnidadeConciliacaoRepository;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;

@Component
public class UnidadeConciliacaoConverter extends JsonConverter<UnidadeConciliacao, UnidadeConciliacaoJson>{
	
	@Autowired
	private UnidadeConciliacaoRepository unidadeConciliacaoRepository;
	
	@Autowired
	private CampoChaveConciliacaoConverter chaveConverter;

	@Autowired
	private CampoConciliacaoConverter conciliavelConverter;
	
	@Autowired
	private CampoInformativoConverter informativoConverter;

	@Override
	public UnidadeConciliacaoJson convertFrom(final UnidadeConciliacao model) {
		List<CampoChaveConciliacaoJson> chavesJson = this.chaveConverter.convertListModelFrom(model.getCamposChave());
		List<CampoConciliacaoJson> conciliaveisJson = this.conciliavelConverter.convertListModelFrom(model.getCamposConciliaveis());
		List<CampoInformativoJson> informativosJson = this.informativoConverter.convertListModelFrom(model.getCamposInformativos());
		return new UnidadeConciliacaoJson(model, chavesJson, conciliaveisJson, informativosJson);
	}

	@Override
	public UnidadeConciliacao convertFrom(final UnidadeConciliacaoJson json) {
		if (json.getId() != null) {
			return this.unidadeConciliacaoRepository.findOne(json.getId());
		}
		throw new ConverterException("Não é possível converter Unidade de Conciliacao Json para Unidade de Conciliação de Model sem o id");
	}
}