package br.com.totvs.cia.parametrizacao.validacao.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class CampoValidacaoArquivoEnumJsonSerializer extends JsonSerializer<CampoValidacaoArquivoEnumJson> {

	@Override
	public void serialize(final CampoValidacaoArquivoEnumJson value, final JsonGenerator jgen,
			final SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeStringField("codigo", value.getCodigo());
		jgen.writeStringField("nome", value.getNome());
		jgen.writeEndObject();
	}

}
