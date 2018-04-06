package br.com.totvs.cia.notificacao.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class StatusEnumJsonDesirializer extends JsonDeserializer<StatusEnumJson> {

	@Override
	public StatusEnumJson deserialize(final JsonParser parser,
			final DeserializationContext context) throws IOException,
			JsonProcessingException {
		TreeNode node = parser.getCodec().readTree(parser);
		String status = node.get("codigo").toString().replaceAll("\"", "");

		return StatusEnumJson.fromCodigo(status);
	}

}
