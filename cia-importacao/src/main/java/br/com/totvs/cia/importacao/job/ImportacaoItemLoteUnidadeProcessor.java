package br.com.totvs.cia.importacao.job;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.importacao.model.CampoImportacao;
import br.com.totvs.cia.importacao.model.CampoLayoutImportacao;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.LoteUnidadeImportacao;
import br.com.totvs.cia.importacao.model.UnidadeImportacao;
import br.com.totvs.cia.importacao.model.UnidadeImportacaoSegregada;
import br.com.totvs.cia.importacao.model.UnidadeLoteImportacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoChaveService;

@Component
public class ImportacaoItemLoteUnidadeProcessor
		implements ItemProcessor<LoteUnidadeImportacao, UnidadeImportacaoSegregada> {

	private static final Logger log = Logger.getLogger(ImportacaoItemLoteUnidadeProcessor.class);
	
	private final ImportacaoService importacaoService;

	private final ParametrizacaoUnidadeImportacaoChaveService parametrizacaoUnidadeImportacaoService;
	
	private List<ParametrizacaoUnidadeImportacaoChave> parametrizacoes;
	
	private Importacao importacao;

	private boolean existeParametrizacaoUnidade;
	
	@Autowired
	public ImportacaoItemLoteUnidadeProcessor(final ImportacaoService importacaoService,
			final ParametrizacaoUnidadeImportacaoChaveService parametrizacaoUnidadeImportacaoChaveService) {
		this.importacaoService = importacaoService;
		this.parametrizacaoUnidadeImportacaoService = parametrizacaoUnidadeImportacaoChaveService;
		
	}
	
	public ItemProcessor<? super LoteUnidadeImportacao, ? extends UnidadeImportacaoSegregada> processor(
			final String importacaoId) {

		this.importacao = this.importacaoService.findOne(importacaoId);
		this.parametrizacoes = this.parametrizacaoUnidadeImportacaoService
				.getByLayout(this.importacao.getLayout().getId());
		this.existeParametrizacaoUnidade = !this.parametrizacoes.isEmpty();
		return this;
	}
	
	@Override
	public UnidadeImportacaoSegregada process(final LoteUnidadeImportacao lote) throws Exception {
		log.info("Efetuando processor do Lote das unidades.");
		if (lote == null) {
			return null;
		}

		final UnidadeImportacaoSegregada unidadeImportacaoSegregada = new UnidadeImportacaoSegregada();
		
		this.gerarUnidadesSegregradas(lote, unidadeImportacaoSegregada);
		
		return unidadeImportacaoSegregada.getUnidades().isEmpty() ? null : unidadeImportacaoSegregada;

	}
	
	private void gerarUnidadesSegregradas(final LoteUnidadeImportacao lote,
			final UnidadeImportacaoSegregada unidadeImportacaoSegregada) {
		lote.getUnidades().forEach(unidade -> this.gerarUnidadeImportacao(unidade, unidadeImportacaoSegregada));
		if (this.segregarUnidadesPorParametrizacao()) {
			unidadeImportacaoSegregada.segregarUnidadesPorParametrizacao(this.parametrizacoes, this.importacao);
		}
	}

	private void gerarUnidadeImportacao(final UnidadeLoteImportacao unidadeLote,
			final UnidadeImportacaoSegregada unidadeImportacaoSegregada) {

		final UnidadeImportacao unidadeImportacao = new UnidadeImportacao();
		unidadeImportacao.adicionarSessao(unidadeLote.getSessao());
		unidadeImportacao.setImportacao(this.importacao);
		for (CampoLayoutImportacao campoLayoutImportacao : unidadeLote.getCampos()) {
			unidadeImportacao.getCampos().add(new CampoImportacao(campoLayoutImportacao.getCampo(), unidadeImportacao, campoLayoutImportacao.getValor(), campoLayoutImportacao.getPattern()));
		}
		unidadeImportacaoSegregada.getUnidades().add(unidadeImportacao);
		if (this.existeParametrizacao()) {
			unidadeImportacaoSegregada.encontrarUnidadeConfiguradaPorParametrizacao(this.parametrizacoes, unidadeLote);
		}
	}
	
	private boolean segregarUnidadesPorParametrizacao() {
		return this.existeParametrizacao();
	}
	
	private boolean existeParametrizacao() {
		return this.existeParametrizacaoUnidade;
	}
}