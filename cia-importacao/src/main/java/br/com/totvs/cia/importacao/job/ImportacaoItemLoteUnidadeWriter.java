package br.com.totvs.cia.importacao.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.importacao.model.UnidadeImportacao;
import br.com.totvs.cia.importacao.model.UnidadeImportacaoSegregada;
import br.com.totvs.cia.importacao.service.UnidadeImportacaoService;

@Component
public class ImportacaoItemLoteUnidadeWriter implements ItemWriter<UnidadeImportacaoSegregada> {
	
	private static final Logger log = Logger.getLogger(ImportacaoItemLoteUnidadeWriter.class);

	@Autowired
	private UnidadeImportacaoService unidadeImportacaoService;
	
	@Override
	public void write(final List<? extends UnidadeImportacaoSegregada> items) throws Exception {
		log.info("Efetuando a gravacao da Unidade de Importacao.");
		for (final UnidadeImportacaoSegregada unidade : items) {
			final List<UnidadeImportacao> unidadesImportacao = unidade.getUnidades();
			this.unidadeImportacaoService.bulkSave(unidadesImportacao);
		}
	}

}
