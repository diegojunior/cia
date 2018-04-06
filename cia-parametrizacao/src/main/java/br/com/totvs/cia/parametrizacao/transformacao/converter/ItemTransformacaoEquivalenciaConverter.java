package br.com.totvs.cia.parametrizacao.transformacao.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.converter.RemetenteConverter;
import br.com.totvs.cia.cadastro.equivalencia.converter.TipoEquivalenciaConverter;
import br.com.totvs.cia.cadastro.equivalencia.json.RemetenteJson;
import br.com.totvs.cia.cadastro.equivalencia.json.TipoEquivalenciaJson;
import br.com.totvs.cia.cadastro.equivalencia.model.Remetente;
import br.com.totvs.cia.cadastro.equivalencia.model.TipoEquivalencia;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.parametrizacao.transformacao.json.ItemTransformacaoEquivalenciaJson;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoEquivalencia;

@Component
public class ItemTransformacaoEquivalenciaConverter extends JsonConverter<ItemTransformacaoEquivalencia, ItemTransformacaoEquivalenciaJson>{
	
	@Autowired
	private RemetenteConverter remetenteConverter;
	
	@Autowired
	private TipoEquivalenciaConverter tipoEquivalenciaConverter;
	
	@Override
	public ItemTransformacaoEquivalencia convertFrom(final ItemTransformacaoEquivalenciaJson json) {
		
		SistemaEnum sistema = SistemaEnum.fromCodigo(json.getSistema().getCodigo());
		Remetente remetente = this.remetenteConverter.convertFrom(json.getRemetente());
		TipoEquivalencia tipoEquivalencia = this.tipoEquivalenciaConverter.convertFrom(json.getTipoEquivalencia());
		
		return new ItemTransformacaoEquivalencia(json.getId(), sistema, remetente, tipoEquivalencia);
	}
	
	@Override
	public ItemTransformacaoEquivalenciaJson convertFrom(final ItemTransformacaoEquivalencia model) {
		
		SistemaJsonEnum sistema = SistemaJsonEnum.fromCodigo(model.getSistema().getCodigo());
		RemetenteJson remetente = this.remetenteConverter.convertFrom(model.getRemetente());
		TipoEquivalenciaJson tipoEquivalencia = this.tipoEquivalenciaConverter.convertFrom(model.getTipoEquivalencia());
		
		return new ItemTransformacaoEquivalenciaJson(model.getId(), sistema, remetente, tipoEquivalencia);
	}
}