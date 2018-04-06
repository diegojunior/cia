package br.com.totvs.cia.parametrizacao.transformacao.converter;

import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.transformacao.json.ItemTransformacaoFixoDeParaJson;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixoDePara;

@Component
public class ItemTransformacaoFixoDeParaConverter extends JsonConverter<ItemTransformacaoFixoDePara, ItemTransformacaoFixoDeParaJson>{
	
	@Override
	public ItemTransformacaoFixoDePara convertFrom(final ItemTransformacaoFixoDeParaJson json) {
		return new ItemTransformacaoFixoDePara(json);
	}
	
	@Override
	public ItemTransformacaoFixoDeParaJson convertFrom(final ItemTransformacaoFixoDePara model) {
		return new ItemTransformacaoFixoDeParaJson(model);
	}
}