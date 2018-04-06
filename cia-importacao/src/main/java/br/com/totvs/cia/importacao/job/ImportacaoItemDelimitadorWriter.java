package br.com.totvs.cia.importacao.job;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.importacao.service.UnidadeLayoutImportacaoService;
@Component
public class ImportacaoItemDelimitadorWriter implements ItemWriter<UnidadeLayoutImportacao>{

	@Autowired
	private UnidadeLayoutImportacaoService unidadeLayoutImportacaoService;
	
	@Override
	public void write(final List<? extends UnidadeLayoutImportacao> items) throws Exception {
		this.unidadeLayoutImportacaoService.bulkSave(items);
	}

}
