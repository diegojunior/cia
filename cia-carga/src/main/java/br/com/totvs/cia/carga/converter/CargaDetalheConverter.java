package br.com.totvs.cia.carga.converter;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.carga.json.CargaCompletaJson;
import br.com.totvs.cia.carga.json.LoteCargaJson;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;

@Component
public class CargaDetalheConverter extends JsonConverter<Carga, CargaCompletaJson>{

	@Autowired
	private LoteCargaConverter loteCargaConverter;
	
	@Override
	public CargaCompletaJson convertFrom(final Carga model) {
		List<LoteCargaJson> lotesJson = this.loteCargaConverter.convertListModelFrom(model.getLotes());
		Collections.sort(lotesJson);
		return new CargaCompletaJson(model, lotesJson);
	}

	@Override
	public Carga convertFrom(final CargaCompletaJson json) {
		throw new ConverterException("Conversão de Json para Model não prevista");
	}
}