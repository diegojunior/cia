package br.com.totvs.cia.parametrizacao.layout.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TipoDelimitadorEnumJsonSerializer extends JsonSerializer<TipoDelimitadorEnumJson> {

	@Override
	public void serialize(final TipoDelimitadorEnumJson value, final JsonGenerator jgen,
			final SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
        jgen.writeStringField("codigo", value.getCodigo());
        jgen.writeStringField("nome", value.getNome());
        jgen.writeEndObject();
		
	}

}
