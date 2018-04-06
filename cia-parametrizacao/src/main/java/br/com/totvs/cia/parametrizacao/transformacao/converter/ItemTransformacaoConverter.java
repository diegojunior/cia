package br.com.totvs.cia.parametrizacao.transformacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.transformacao.json.ItemTransformacaoJson;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacao;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixo;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoEquivalencia;

@Component
public class ItemTransformacaoConverter extends JsonConverter<ItemTransformacao, ItemTransformacaoJson> {

	@Autowired
	private ItemTransformacaoFixoConverter itemFixoConverter;

	@Autowired
	private ItemTransformacaoEquivalenciaConverter itemServicoConverter;

	@Override
	public ItemTransformacao convertFrom(final ItemTransformacaoJson json) {
		
		if (json.isTipoTransformacaoFixo()) {
			
			return this.itemFixoConverter.convertFrom(json.getItemFixo());
		}

		return this.itemServicoConverter.convertFrom(json.getItemEquivalencia());
	}

	@Override
	public ItemTransformacaoJson convertFrom(final ItemTransformacao model) {

		if (model.isTipoTransformacaoFixo()) {
			
			return new ItemTransformacaoJson(this.itemFixoConverter.convertFrom((ItemTransformacaoFixo) model));
			
		}
		
		return new ItemTransformacaoJson(this.itemServicoConverter.convertFrom((ItemTransformacaoEquivalencia) model));
	}
}