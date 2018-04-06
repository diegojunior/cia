package br.com.totvs.cia.parametrizacao.dominio.model;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

public class Dominios implements Iterable<Dominio> {
	
	private static final Logger log = Logger.getLogger(Dominios.class);

	private final List<Dominio> dominios;

	public Dominios(final List<Dominio> dominios) {
		this.dominios = dominios;
	}

	@Override
	public Iterator<Dominio> iterator() {
		return this.dominios.iterator();
	}

	public Dominio getBy(final String codigo) {
		try {
			 return Iterables.find(this.dominios, new Predicate<Dominio>() {
				@Override
				public boolean apply(final Dominio dominio) {
					return dominio.getCodigo().equalsIgnoreCase(codigo);
				}
			});
			 
		} catch (final NoSuchElementException e){
			log.warn(String.format("Dominio não encontrado. Campo '%s'", codigo));
			return null;
			/*log.info("Verificando se possui sinonimo...'");
			return getSinonimosBy(codigo);*/
		}
	}
}
