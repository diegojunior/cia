package br.com.totvs.cia.cadastro.grupo.model;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class Grupos implements Iterable<Grupo> {
	
	private static final Logger log = Logger.getLogger(Grupos.class);

	private List<Grupo> grupos;
	
	public Grupos(final List<Grupo> grupos) {
		this.grupos = grupos;
	}

	@Override
	public Iterator<Grupo> iterator() {
		return grupos.iterator();
	}

	public Grupo getBy(final String codigo) {
		try {
			 return Iterables.find(this.grupos, new Predicate<Grupo>() {
				@Override
				public boolean apply(final Grupo grupo) {
					return grupo.getCodigo().equals(codigo);
				}
			});
		} catch (NoSuchElementException e){
			log.warn(String.format("Grupo não encontrado. Codigo '%s'", codigo));
			return null;
		}
	}

	public List<Grupo> add(List<Grupo> grupos) {
		List<Grupo> gruposCarregados = Lists.newArrayList();
		for (Grupo grupo : grupos) {
			gruposCarregados.add(grupo);
		}
		for (Grupo grupo : this.grupos) {
			if(!gruposCarregados.contains(grupo)){
				gruposCarregados.add(grupo);
			}
		}
		return gruposCarregados;
	}
}
