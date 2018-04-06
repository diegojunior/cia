package br.com.totvs.cia.parametrizacao.transformacao.converter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.transformacao.json.ItemTransformacaoFixoDeParaJson;
import br.com.totvs.cia.parametrizacao.transformacao.json.ItemTransformacaoFixoJson;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixo;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixoDePara;
import br.com.totvs.cia.parametrizacao.transformacao.repository.ItemTransformacaoFixoRepository;

@Component
public class ItemTransformacaoFixoConverter extends JsonConverter<ItemTransformacaoFixo, ItemTransformacaoFixoJson>{
	
	@Autowired
	private ItemTransformacaoFixoRepository itemTransformacaoFixoRepository;
	
	@Autowired
	private ItemTransformacaoFixoDeParaConverter itemTransformacaoFixoDeParaConverter;
	
	@Override
	public ItemTransformacaoFixo convertFrom(final ItemTransformacaoFixoJson json) {
		
		final List<ItemTransformacaoFixoDePara> itensDePara = this.itemTransformacaoFixoDeParaConverter.convertListJsonFrom(json.getItens());
		
		if (json.getId() != null) {
			final ItemTransformacaoFixo model = this.itemTransformacaoFixoRepository.findOne(json.getId());
			model.atualizarItens(itensDePara);
			return model;
		}
		
		return new ItemTransformacaoFixo(itensDePara);
	}
	
	@Override
	public ItemTransformacaoFixoJson convertFrom(final ItemTransformacaoFixo model) {
		
		final List<ItemTransformacaoFixoDeParaJson> itensDePara = this.itemTransformacaoFixoDeParaConverter.convertListModelFrom(model.getItensDePara());
		
		return new ItemTransformacaoFixoJson(model.getId(), itensDePara);
	}
}