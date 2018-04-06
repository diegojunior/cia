package br.com.totvs.cia.carga.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.configuracaoservico.converter.ConfiguracaoServicoConverter;
import br.com.totvs.cia.cadastro.configuracaoservico.json.ConfiguracaoServicoJson;
import br.com.totvs.cia.carga.json.LoteCargaJson;
import br.com.totvs.cia.carga.json.LoteClienteJson;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;

@Component
public class LoteCargaConverter extends JsonConverter<LoteCarga, LoteCargaJson>{

	@Autowired
	private ConfiguracaoServicoConverter servicoConverter;
	
	@Autowired
	private LoteClienteConverter loteCarteiraConverter;
	
	@Override
	public LoteCargaJson convertFrom(final LoteCarga model) {

		ConfiguracaoServicoJson servicoJson = this.servicoConverter.convertFrom(model.getServico());
		
		List<LoteClienteJson> lotesCarteirasJson = loteCarteiraConverter.convertListModelFrom(model.getLotesClientes());
		
		return new LoteCargaJson(model, servicoJson, lotesCarteirasJson);
	}

	@Override
	public LoteCarga convertFrom(final LoteCargaJson json) {
		throw new ConverterException("Conversão de Json para Model não prevista");
	}
}