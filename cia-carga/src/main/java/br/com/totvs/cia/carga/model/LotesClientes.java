package br.com.totvs.cia.carga.model;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class LotesClientes implements Iterable<LoteCliente> {
	
	private List<LoteCliente> lotes;
	
	private LotesClientes(final List<LoteCliente> lotes) {
		this.lotes = lotes;
	}
	
	private LotesClientes() {
		this.lotes = Lists.newArrayList();;
	}
	
	public LotesClientes(final LoteCarga loteCarga, final List<String> clientes) {
		this.lotes = Lists.newArrayList();
		for (String cliente : clientes) {
			this.lotes.add(new LoteCliente(loteCarga, cliente));
		} 
	}
	
	public static LotesClientes build (final List<LoteCliente> lotes) {
		return new LotesClientes(lotes);
	}
	
	public static LotesClientes build() {
		return new LotesClientes();
	}

	public static List<LoteCliente> build(final LoteCarga loteCarga, final List<String> clientes) {
		return Lists.newArrayList(new LotesClientes(loteCarga, clientes));
	}
	
	public void add(final LoteCliente loteCliente) {
		this.lotes.add(loteCliente);
	}
	
	@Override
	public Iterator<LoteCliente> iterator() {
		return this.lotes.iterator();
	}
	
	public Integer size() {
		return this.lotes.size();
	}
	
	public LoteCliente get(final Integer index) {
		return this.lotes.get(index);
	}

	public Boolean contains(final LoteCliente loteCliente) {
		for (LoteCliente lote : lotes) {
			if (lote.getCliente().equalsIgnoreCase(loteCliente.getCliente()) &&
					lote.getLoteCarga().getId().equals(loteCliente.getLoteCarga().getId())) {
				return true;
			}
		}
		return false;
	}

	public LoteCliente get(final String cliente, final LoteCarga loteCarga) {
		for (LoteCliente lote : lotes) {
			if (lote.getCliente().equalsIgnoreCase(cliente) &&
					lote.getLoteCarga().getId().equals(loteCarga.getId())) {
				return lote;
			}
		}
		return null;
	}
}