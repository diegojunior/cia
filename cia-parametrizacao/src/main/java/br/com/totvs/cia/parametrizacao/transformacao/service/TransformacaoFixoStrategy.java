package br.com.totvs.cia.parametrizacao.transformacao.service;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixo;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixoDePara;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;
import br.com.totvs.cia.parametrizacao.transformacao.model.TransformacaoStrategy;

public class TransformacaoFixoStrategy implements TransformacaoStrategy {

	private ItemTransformacaoFixo item;
	
	@Override
	public void loadItens(final SistemaEnum sistema, final Transformacao transformacao) {
		this.item = (ItemTransformacaoFixo) transformacao.getItem();
	}

	@Override
	public String transform(final String valor) {
		for (final ItemTransformacaoFixoDePara dePara : this.item.getItensDePara()) {
			if (dePara.getDe().equals(valor))
				return dePara.getPara();
		}
		return valor;
	}

}
