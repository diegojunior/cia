package br.com.totvs.cia.carga.model;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;

public class LotesCarga implements Iterable<LoteCarga> {
	
	private static final Logger log = Logger.getLogger(LotesCarga.class);
	
	private List<LoteCarga> lotes;
	
	private LotesCarga(final List<LoteCarga> lotes) {
		this.lotes = lotes;
	}
	
	public LotesCarga(final Carga carga, final List<ConfiguracaoServico> servicos) {
		this.lotes = Lists.newArrayList();
		for (ConfiguracaoServico servico : servicos) {
			this.lotes.add(new LoteCarga(carga, servico));
		} 
	}
	
	public LotesCarga(final Carga carga, final List<ConfiguracaoServico> servicos, final List<String> clientes) {
		this.lotes = Lists.newArrayList();
		for (ConfiguracaoServico servico : servicos) {
			this.lotes.add(new LoteCarga(carga, servico, clientes));
		} 
	}
	
	public static LotesCarga build (final List<LoteCarga> lotes) {
		return new LotesCarga(lotes);
	}
	
	public static List<LoteCarga> build(Carga carga, List<ConfiguracaoServico> servicos) {
		return Lists.newArrayList(new LotesCarga(carga, servicos));
	}

	public static List<LoteCarga> build(Carga carga, List<ConfiguracaoServico> servicos, final List<String> clientes) {
		return Lists.newArrayList(new LotesCarga(carga, servicos, clientes));
	}
	
	@Override
	public Iterator<LoteCarga> iterator() {
		return this.lotes.iterator();
	}
	
	public LoteCarga get(final Integer index) {
		return this.lotes.get(index);
	}

	public LoteCarga getBy(final ConfiguracaoServico servico) {
		try {
			return Iterables.find(this.lotes, new Predicate<LoteCarga>() {
				@Override
				public boolean apply(final LoteCarga lote) {
					return lote.getServico().getId().equals(servico.getId());
				}
			});
		} catch (NoSuchElementException e){
			log.warn(String.format("Lote com o Servico '%s' não encontrado.", servico.getCodigo()));
			return null;
		}
	}
	
	public Integer size() {
		return this.lotes.size();
	}
	
	public Boolean isLast(LoteCarga loteParam) {
		return Iterators.getLast(this.lotes.iterator()).equals(loteParam);
	}

	public Boolean isEmpty() {
		for(LoteCarga lote : this.lotes) {
			if (!lote.getLotesClientes().isEmpty()) {
				return false; 
			}
		}
		return true;
	}
}