package br.com.totvs.cia.importacao.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.LoteUnidadeImportacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.importacao.model.UnidadeLoteImportacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.importacao.service.UnidadeLayoutImportacaoService;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoChaveService;

@Component
public class ImportacaoItemLoteUnidadeReader implements ItemReader<LoteUnidadeImportacao> {

	private static final Logger log = Logger.getLogger(ImportacaoItemLoteUnidadeReader.class);

	@Autowired
	private ImportacaoService importacaoService;

	@Autowired
	private UnidadeLayoutImportacaoService unidadeLayoutImportacaoService;
	
	@Autowired
	private ParametrizacaoUnidadeImportacaoChaveService parametrizacaoUnidadeImportacaoService;
	
	private boolean existeParametrizacaoUnidade;
	
	private int indexChunk;
	
	private int chunkLote;
	
	private Importacao importacao;

	
	public ItemReader<LoteUnidadeImportacao> reader(final String importacaoId, final int chunkLote) {
		this.importacao = this.importacaoService.getOne(importacaoId);
		List<ParametrizacaoUnidadeImportacaoChave> parametrizacoes = this.parametrizacaoUnidadeImportacaoService.getByLayout(this.importacao.getLayout().getId());
		this.existeParametrizacaoUnidade = !parametrizacoes.isEmpty();
		this.indexChunk = 0;
		this.chunkLote = chunkLote;
		return this;
	}

	@Override
	public LoteUnidadeImportacao read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("Efetuando a leitura do Lote das unidades.");
		final LoteUnidadeImportacao lote = new LoteUnidadeImportacao();
		this.gerarLote(lote);
		if (lote.getUnidades().isEmpty())
			return null;
		return lote;
		
	}
	
	private LoteUnidadeImportacao gerarLote(final LoteUnidadeImportacao lote) {
		final Pageable pageRequest = new PageRequest(this.indexChunk, this.chunkLote);
		final List<UnidadeLayoutImportacao> unidadesByImportacao = this.unidadeLayoutImportacaoService.getUnidadesByImportacao(this.importacao, pageRequest);
		if (unidadesByImportacao.isEmpty()) {
			return lote;
		}
		unidadesByImportacao
			.forEach(unidade -> {
				UnidadeLoteImportacao unidadeLote = new UnidadeLoteImportacao(unidade.getSessao(), unidade.getCampos());
				lote.adicionarUnidadeLote(unidadeLote);
			});
		this.indexChunk++;
		
		if (this.existeParametrizacaoUnidade) 
			return this.gerarLote(lote);
		
		return lote;
	}
	
}
