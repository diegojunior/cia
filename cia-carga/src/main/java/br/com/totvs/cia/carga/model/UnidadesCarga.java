package br.com.totvs.cia.carga.model;

import java.util.Iterator;
import java.util.List;

public class UnidadesCarga implements Iterable<UnidadeCarga> {
	
	private List<UnidadeCarga> unidades;
	
	private UnidadesCarga(final List<UnidadeCarga> unidades) {
		this.unidades = unidades;
	}
	
	public static UnidadesCarga build(final List<UnidadeCarga> unidades) {
		return new UnidadesCarga(unidades);
	}
	
	@Override
	public Iterator<UnidadeCarga> iterator() {
		return this.unidades.iterator();
	}

	public void clear() {
	}
}