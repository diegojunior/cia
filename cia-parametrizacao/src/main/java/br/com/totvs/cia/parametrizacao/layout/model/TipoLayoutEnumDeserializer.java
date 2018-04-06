package br.com.totvs.cia.parametrizacao.layout.model;

import java.io.IOException;

import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class TipoLayoutEnumDeserializer extends JsonDeserializer<TipoLayoutEnum>{

	@Override
	public TipoLayoutEnum deserialize(JsonParser parser,
			DeserializationContext context) throws IOException,
			JsonProcessingException {
		
		TreeNode node = parser.getCodec().readTree(parser);
		String codigo = node.get("codigo").toString().replaceAll("\"","");
				
		return TipoLayoutEnum.fromCodigo(codigo);
	}

}
