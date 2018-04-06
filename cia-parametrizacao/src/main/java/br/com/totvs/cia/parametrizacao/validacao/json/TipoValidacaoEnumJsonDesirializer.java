package br.com.totvs.cia.parametrizacao.validacao.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class TipoValidacaoEnumJsonDesirializer extends JsonDeserializer<TipoValidacaoEnumJson> {

	@Override
	public TipoValidacaoEnumJson deserialize(final JsonParser parser,
			final DeserializationContext context) throws IOException,
			JsonProcessingException {
		TreeNode node = parser.getCodec().readTree(parser);
		String codigo = node.get("codigo").toString().replaceAll("\"", "");

		return TipoValidacaoEnumJson.fromCodigo(codigo);
	}

}
