package br.com.totvs.cia.conciliacao.model;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class ConciliacoesForExecution implements Iterable<ConciliacaoForExecution> {
	
	private List<ConciliacaoForExecution> conciliacoes;

	public ConciliacoesForExecution(final List<ConciliacaoForExecution> conciliacoes) {
		this.conciliacoes = conciliacoes;
	}

	@Override
	public Iterator<ConciliacaoForExecution> iterator() {
		return conciliacoes.iterator();
	}
	
	public List<ConciliacaoForExecution> getBy(final String perfil) {
		if (perfil != null && !"".equals(perfil.trim())) {
			List<ConciliacaoForExecution> conciliacoesForExecution = Lists.newArrayList();
			for (ConciliacaoForExecution conciliacaoForExecution : this.conciliacoes) {
				if (conciliacaoForExecution.getPerfil().equals(perfil)) {
					conciliacoesForExecution.add(conciliacaoForExecution);
				}
			}
			return conciliacoesForExecution;
		}
		return this.conciliacoes;
	}
}