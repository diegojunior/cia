package br.com.totvs.cia.parametrizacao.perfilconciliacao.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class LocalizacaoCamposJsonEnumSerializer extends JsonSerializer<LocalizacaoCamposJsonEnum> {

	@Override
	public void serialize(final LocalizacaoCamposJsonEnum value, final JsonGenerator jgen,
			final SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
        jgen.writeStringField("codigo", value.getCodigo());
        jgen.writeStringField("nome", value.getNome());
        jgen.writeEndObject();
		
	}

}
