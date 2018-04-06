package br.com.totvs.cia.importacao.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class StatusImportacaoEnumJsonSerializer extends JsonSerializer<StatusImportacaoJsonEnum> {

	@Override
	public void serialize(final StatusImportacaoJsonEnum value, final JsonGenerator jgen,
			final SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeStringField("codigo", value.getCodigo());
		jgen.writeStringField("nome", value.getNome());
		jgen.writeEndObject();
	}

}
