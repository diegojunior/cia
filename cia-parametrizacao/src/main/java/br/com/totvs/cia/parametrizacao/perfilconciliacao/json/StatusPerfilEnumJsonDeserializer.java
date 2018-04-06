package br.com.totvs.cia.parametrizacao.perfilconciliacao.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class StatusPerfilEnumJsonDeserializer extends JsonDeserializer<StatusPerfilJsonEnum> {

	@Override
	public StatusPerfilJsonEnum deserialize(final JsonParser parser,
			final DeserializationContext context) throws IOException,
			JsonProcessingException {
		TreeNode node = parser.getCodec().readTree(parser);
		String status = node.get("codigo").toString().replaceAll("\"", "");

		return StatusPerfilJsonEnum.fromCodigo(status);
	}

}
