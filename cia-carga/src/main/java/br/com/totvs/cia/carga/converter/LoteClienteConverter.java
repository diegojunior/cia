package br.com.totvs.cia.carga.converter;

import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.carteira.json.ClienteJson;
import br.com.totvs.cia.carga.json.LoteClienteJson;
import br.com.totvs.cia.carga.json.StatusLoteClienteJsonEnum;
import br.com.totvs.cia.carga.model.LoteCliente;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;
import br.com.totvs.cia.infra.util.DateUtil;

@Component
public class LoteClienteConverter extends JsonConverter<LoteCliente, LoteClienteJson>{

		@Override
	public LoteClienteJson convertFrom(final LoteCliente model) {

		ClienteJson carteiraJson = new ClienteJson(model.getCliente());
		StatusLoteClienteJsonEnum status = model.getStatus() != null ? StatusLoteClienteJsonEnum.fromCodigo(model.getStatus().getCodigo()) : null;
		String dataProcessamento = DateUtil.format(model.getDataProcessamento(), DateUtil.yyyy_MM_dd);
		
		return new LoteClienteJson(model.getId(), carteiraJson, dataProcessamento, status);
	}

	@Override
	public LoteCliente convertFrom(final LoteClienteJson json) {
		throw new ConverterException("Conversão de Json para Model não prevista");
	}
}