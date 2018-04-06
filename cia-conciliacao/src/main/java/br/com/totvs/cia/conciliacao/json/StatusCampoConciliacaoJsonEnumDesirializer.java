package br.com.totvs.cia.conciliacao.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class StatusCampoConciliacaoJsonEnumDesirializer extends JsonDeserializer<StatusCampoConciliacaoJsonEnum> {

	@Override
	public StatusCampoConciliacaoJsonEnum deserialize(final JsonParser parser,
			final DeserializationContext context) throws IOException,
			JsonProcessingException {
		TreeNode node = parser.getCodec().readTree(parser);
		String codigo = node.get("codigo").toString().replaceAll("\"", "");

		return StatusCampoConciliacaoJsonEnum.fromCodigo(codigo);
	}

}
