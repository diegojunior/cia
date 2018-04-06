package br.com.totvs.cia.cadastro.base.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class SistemaJsonEnumSerializer extends JsonSerializer<SistemaJsonEnum> {

	@Override
	public void serialize(final SistemaJsonEnum value, final JsonGenerator jgen,
			final SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeStringField("codigo", value.getCodigo());
		jgen.writeStringField("nome", value.getNome());
		jgen.writeEndObject();
	}

}
