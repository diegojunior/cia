package br.com.totvs.cia.cadastro.carteira.model;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.log4j.Logger;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

public class Carteiras implements Iterable<Cliente> {
	
	private static final Logger log = Logger.getLogger(Carteiras.class);

	private List<Cliente> clientes;
	
	public Carteiras(final List<Cliente> clientes) {
		this.clientes = clientes;
	}

	@Override
	public Iterator<Cliente> iterator() {
		return clientes.iterator();
	}

	public Cliente getBy(final String codigo) {
		try {
			 return Iterables.find(this.clientes, new Predicate<Cliente>() {
				@Override
				public boolean apply(final Cliente cliente) {
					return cliente.getCodigo().equals(codigo);
				}
			});
		} catch (NoSuchElementException e){
			log.warn(String.format("Remetente não encontrado. Codigo '%s'", codigo));
			return null;
		}
	}

	public List<Cliente> add(List<Cliente> clientes) {
		
		List<Cliente> clientesCarregados = Lists.newArrayList();
		
		for (Cliente cliente : clientes) {
			//cliente.setLoad(true);
			clientesCarregados.add(cliente);
		}
		
		for (Cliente cliente : this.clientes) {
			if(!clientesCarregados.contains(cliente)){
				//cliente.setLoad(false);
				clientesCarregados.add(cliente);
			}
		}
		
		return clientesCarregados;
	}
}
