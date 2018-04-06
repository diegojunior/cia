package br.com.totvs.cia.importacao.job;

import java.util.Iterator;
import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import br.com.totvs.cia.importacao.model.BlocoDelimitador;
import br.com.totvs.cia.importacao.model.BlocoIndividualUnidadeImportacao;
import br.com.totvs.cia.importacao.model.BlocoLoteUnidadeImportacao;
import br.com.totvs.cia.importacao.model.BlocoUnidade;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.importacao.service.UnidadeLayoutImportacaoService;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoBlocoService;

@Component
public class ImportacaoItemBlocoReader implements ItemReader<BlocoDelimitador>{

	public static final int SIZE = 300;

	private final int maxItemCount = Integer.MAX_VALUE;
	
	@Autowired
	private ImportacaoService importacaoService;
	
	@Autowired
	private ParametrizacaoUnidadeImportacaoBlocoService parametrizacaoUnidadeBlocoService;
	
	@Autowired
	private UnidadeLayoutImportacaoService unidadeLayoutImportacaoService;
	
	private Importacao importacao;
	
	private ParametrizacaoUnidadeImportacaoBloco parametrizacaoBloco;
	
	private final List<UnidadeLayoutImportacao> unidadesByImportacao = Lists.newArrayList();

	private int currentItemCount = 0;
	
	public ItemReader<BlocoDelimitador> reader(final String importacaoId) {
		this.importacao = this.importacaoService.getOne(importacaoId);
		this.parametrizacaoBloco = this.parametrizacaoUnidadeBlocoService.getByLayout(this.importacao.getLayout().getId());
		this.currentItemCount = 0;
		return this;
	}
	
	@Override
	public BlocoDelimitador read()
			throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		return this.doRead();
	}

	private BlocoDelimitador doRead() {
		if (this.currentItemCount >= this.maxItemCount) 
			return null;
		
		if (this.parametrizacaoBloco != null)
			return this.doReadBloco(new BlocoLoteUnidadeImportacao());
		
		return this.doReadEachLine();
	}
	
	private BlocoDelimitador doReadEachLine() {
		BlocoIndividualUnidadeImportacao blocoIndividual = new BlocoIndividualUnidadeImportacao();
		final Pageable pageRequest = new PageRequest(this.currentItemCount, SIZE);
		List<UnidadeLayoutImportacao> unidades = this.unidadeLayoutImportacaoService.obtemUnidadesLayoutOrdenadaNumeroLinha(this.importacao, pageRequest);
		Iterator<UnidadeLayoutImportacao> iterator = unidades.iterator();
		while (iterator.hasNext()) {
			UnidadeLayoutImportacao unidade = iterator.next();
			blocoIndividual.addUnidadeLayoutImportacao(unidade);
			iterator.remove();
		}
		this.currentItemCount++;
		
		if (blocoIndividual.getUnidadesLayoutImportacao().isEmpty())
			return null;
		
		return blocoIndividual;
	}

	private BlocoDelimitador doReadBloco(final BlocoLoteUnidadeImportacao lote) {
		final Pageable pageRequest = new PageRequest(this.currentItemCount, SIZE);
		this.unidadesByImportacao.addAll(this.unidadeLayoutImportacaoService.obtemUnidadesLayoutOrdenadaNumeroLinha(this.importacao, pageRequest));
		Sessao fechamentoBloco = this.parametrizacaoBloco.getFechamento();
		int indice = 0;
		int ultimaPosicaoFechamentoBloco = this.obtemPosicaoUltimoGrupoCompleto(this.unidadesByImportacao, fechamentoBloco);
		Iterator<UnidadeLayoutImportacao> iterator = this.unidadesByImportacao.iterator();
		List<UnidadeLayoutImportacao> unidadesBloco = Lists.newArrayList();
		while (iterator.hasNext() && indice <= ultimaPosicaoFechamentoBloco) {
			UnidadeLayoutImportacao unidade = iterator.next();
			iterator.remove();
			indice++;
			if (unidade.getSessao().equals(fechamentoBloco)) {
				BlocoUnidade blocoUnidade = new BlocoUnidade();
				blocoUnidade.getUnidades().addAll(unidadesBloco);
				lote.addBlocoUnidades(blocoUnidade);
				unidadesBloco.clear();
			} else {
				unidadesBloco.add(unidade);
			}
		}
		this.currentItemCount++;
		
		if (lote.getBlocos().isEmpty())
			return null;
		
		return lote;
	}

	private int obtemPosicaoUltimoGrupoCompleto(final List<UnidadeLayoutImportacao> unidadesByImportacao,
			final Sessao fechamentoBloco) {
		if (unidadesByImportacao.isEmpty()) 
			return 0;
		
		int posicaoUltimoElemento = unidadesByImportacao.size() - 1;
		UnidadeLayoutImportacao ultimaUnidade = unidadesByImportacao.get(posicaoUltimoElemento);
		if (ultimaUnidade.getSessao().equals(fechamentoBloco)) {
			return posicaoUltimoElemento;
		} 
		List<UnidadeLayoutImportacao> subList = unidadesByImportacao.subList(0, posicaoUltimoElemento);
		return this.obtemPosicaoUltimoGrupoCompleto(subList, fechamentoBloco);
	}

}
