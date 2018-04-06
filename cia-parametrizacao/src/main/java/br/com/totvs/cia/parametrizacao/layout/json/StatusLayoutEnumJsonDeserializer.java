package br.com.totvs.cia.parametrizacao.layout.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class StatusLayoutEnumJsonDeserializer extends JsonDeserializer<StatusLayoutEnumJson>{

	@Override
	public StatusLayoutEnumJson deserialize(JsonParser parser,
			DeserializationContext context) throws IOException,
			JsonProcessingException {
		
		TreeNode node = parser.getCodec().readTree(parser);
		String codigo = node.get("codigo").toString().replaceAll("\"","");
				
		return StatusLayoutEnumJson.fromCodigo(codigo);
	}

}
