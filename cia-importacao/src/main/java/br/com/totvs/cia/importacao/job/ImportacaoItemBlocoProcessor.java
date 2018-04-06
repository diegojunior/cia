package br.com.totvs.cia.importacao.job;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.importacao.model.BlocoDelimitador;
import br.com.totvs.cia.importacao.model.BlocoUnidade;
import br.com.totvs.cia.importacao.model.CampoImportacao;
import br.com.totvs.cia.importacao.model.CampoLayoutImportacao;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.UnidadeImportacao;
import br.com.totvs.cia.importacao.model.UnidadeImportacaoSegregada;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.LinhaBlocoParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoBlocoService;

@Component
public class ImportacaoItemBlocoProcessor implements ItemProcessor<BlocoDelimitador, UnidadeImportacaoSegregada> {
	
private static final Logger LOG = Logger.getLogger(ImportacaoItemBlocoProcessor.class);
	
	@Autowired
	private ImportacaoService importacaoService;

	@Autowired
	private ParametrizacaoUnidadeImportacaoBlocoService parametrizacaoUnidadeBlocoService;
	
	private ParametrizacaoUnidadeImportacaoBloco parametrizacaoBloco;

	private Importacao importacao;
	
	public ItemProcessor<? super BlocoDelimitador, ? extends UnidadeImportacaoSegregada> processor(
			final String importacaoId) {
		this.importacao = this.importacaoService.findOne(importacaoId);
		this.parametrizacaoBloco = this.parametrizacaoUnidadeBlocoService.getByLayout(this.importacao.getLayout().getId());
		return this;
	}
	
	@Override
	public UnidadeImportacaoSegregada process(final BlocoDelimitador bloco) throws Exception {
		LOG.info("Efetuando processor do Lote das unidades.");
		if (bloco == null) {
			return null;
		}
		
		if (this.parametrizacaoBloco != null) {
			return this.doProcessBloco(bloco);
		}
		
		return this.doProcessIndividual(bloco);
	}
	
	private UnidadeImportacaoSegregada doProcessBloco(final BlocoDelimitador blocoDelimitador) {
		final UnidadeImportacaoSegregada unidadeImportacaoSegregada = new UnidadeImportacaoSegregada();
		
		for (BlocoUnidade bloco : blocoDelimitador.getBlocos()) {
			LinhaBlocoParametrizacaoUnidadeImportacao linhaConfiguradaSessaoPosicao = this.parametrizacaoBloco.getLinhaConfiguradaSessaoPosicao();
			LinhaBlocoParametrizacaoUnidadeImportacao linhaConfiguradaSessaoAbertura = this.parametrizacaoBloco.getLinhaConfiguradaSessaoAbertura();
			
			UnidadeLayoutImportacao unidadeLayoutImportacao = this.filtrarUnidadeConfiguradaComoLinhaSessaoAbertura(bloco, linhaConfiguradaSessaoAbertura);
			List<UnidadeLayoutImportacao> unidadesPosicao = this.filtrarUnidadeConfiguradaComoLinhaPosicao(bloco, linhaConfiguradaSessaoPosicao);
			
			for (UnidadeLayoutImportacao unidadeLayout : unidadesPosicao) {
				final UnidadeImportacao unidadeImportacao = this.gerarUnidade(unidadeImportacaoSegregada);
				
				Map<Campo, CampoLayoutImportacao> camposSessaoPosicaoSegregados = unidadeLayout.getCamposIguaisDe(linhaConfiguradaSessaoPosicao.getCampos());
				Map<Campo, CampoLayoutImportacao> camposSessaoAberturaAgregados = unidadeLayoutImportacao.getCamposIguaisDe(linhaConfiguradaSessaoAbertura.getCampos());
				
				unidadeImportacao.convertAndAdd(camposSessaoPosicaoSegregados.values());
				unidadeImportacao.convertAndAdd(camposSessaoAberturaAgregados.values());
				
			}
		}

		return unidadeImportacaoSegregada;
	}
	
	private UnidadeImportacaoSegregada doProcessIndividual(final BlocoDelimitador bloco) {
		final UnidadeImportacaoSegregada unidadeImportacaoSegregada = new UnidadeImportacaoSegregada();
		for (UnidadeLayoutImportacao unidade : bloco.getUnidadesLayoutImportacao()) {
			UnidadeImportacao unidadeImportacao = this.gerarUnidade(unidadeImportacaoSegregada);
			for (CampoLayoutImportacao campoLayoutImportacao : unidade.getCampos()) {
				unidadeImportacao.getCampos().add(new CampoImportacao(campoLayoutImportacao.getCampo(), 
						unidadeImportacao, campoLayoutImportacao.getValor(), campoLayoutImportacao.getPattern()));
			}
		}
 		
		return unidadeImportacaoSegregada;
	}

	private UnidadeLayoutImportacao filtrarUnidadeConfiguradaComoLinhaSessaoAbertura(final BlocoUnidade bloco,
			final LinhaBlocoParametrizacaoUnidadeImportacao linhaConfiguradaAbertura) {
		return bloco
				.getUnidades()
				.stream()
				.filter(unidade -> unidade.getSessao().equals(linhaConfiguradaAbertura.getSessao()))
				.findAny()
				.get();
	}
	
	private List<UnidadeLayoutImportacao> filtrarUnidadeConfiguradaComoLinhaPosicao(final BlocoUnidade bloco,
			final LinhaBlocoParametrizacaoUnidadeImportacao linhaConfiguradaNaoAbertura) {
		return bloco
			.getUnidades()
			.stream()
			.filter(unidade -> unidade.getSessao().equals(linhaConfiguradaNaoAbertura.getSessao()))
			.collect(Collectors.toList());
	}
	
	private UnidadeImportacao gerarUnidade(final UnidadeImportacaoSegregada unidadeImportacaoSegregada) {
		final UnidadeImportacao unidadeImportacao = new UnidadeImportacao();
		unidadeImportacaoSegregada.getUnidades().add(unidadeImportacao);
		unidadeImportacao.setImportacao(this.importacao);
		unidadeImportacao.adicionarParametrizacaoUnidadeBloco(this.parametrizacaoBloco);
		return unidadeImportacao;
	}
}