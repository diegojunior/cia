package br.com.totvs.cia.parametrizacao.transformacao.model;

import br.com.totvs.cia.infra.model.Model;

public interface ItemTransformacao extends Model {
	
	Boolean isTipoTransformacaoFixo();
	
	Boolean isTipoTransformacaoEquivalencia();
	
	ItemTransformacao of (final Transformacao transformacao);
	
}