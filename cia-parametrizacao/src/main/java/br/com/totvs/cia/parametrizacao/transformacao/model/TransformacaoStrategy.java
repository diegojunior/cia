package br.com.totvs.cia.parametrizacao.transformacao.model;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;

public interface TransformacaoStrategy {

	public void loadItens(SistemaEnum sistema, Transformacao transformacao);
	
	public String transform(String valor);
	
}
