package br.com.totvs.cia.parametrizacao.transformacao.service;

import org.springframework.stereotype.Service;

import br.com.totvs.cia.infra.exception.CiaBusinessException;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacao;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoFixo;

@Service
public class ItemTransformacaoService {
	
	public void validaDuplicidade(final ItemTransformacao item) {
		
		if (item.isTipoTransformacaoFixo()) {
			String deDuplicado = ((ItemTransformacaoFixo) item).isPossuiDuplicidade();
			
			if(deDuplicado != null) {
				throw new CiaBusinessException(String.format("O sistema já possui um campo De [ %s ]", deDuplicado));
			}
		}
	}
}
