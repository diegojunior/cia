package br.com.totvs.cia.cadastro.tipo;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class TipoValorDominioEnumSerializer extends JsonSerializer<TipoValorDominioEnum> {

	@Override
	public void serialize(final TipoValorDominioEnum value, final JsonGenerator jgen,
			final SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jgen.writeStartObject();
		jgen.writeStringField("codigo", value.getCodigo());
		jgen.writeStringField("nome", value.getNome());
		jgen.writeNumberField("noScale", value.getNoScale());
		jgen.writeNumberField("scale", value.getScale());
		jgen.writeNumberField("precisaoPadrao", value.getPrecisaoPadrao());
		jgen.writeEndObject();
	}

}
