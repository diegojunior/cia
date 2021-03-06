package br.com.totvs.cia.parametrizacao.layout.model;

import java.io.IOException;

import br.com.totvs.cia.parametrizacao.layout.model.DelimitadorEnum;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DelimitadorEnumSerializer extends JsonSerializer<DelimitadorEnum> {

	@Override
	public void serialize(DelimitadorEnum value, JsonGenerator jgen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
        jgen.writeStringField("codigo", value.getCodigo());
        jgen.writeStringField("nome", value.getNome());
        jgen.writeEndObject();
		
	}

}
