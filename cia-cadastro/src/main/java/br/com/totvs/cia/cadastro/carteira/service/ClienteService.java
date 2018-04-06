package br.com.totvs.cia.cadastro.carteira.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.carteira.model.Cliente;
import br.com.totvs.cia.cadastro.carteira.repository.CarteiraSpecification;
import br.com.totvs.cia.cadastro.carteira.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public <S extends Cliente> S save(final S entity) {
		return this.clienteRepository.save(entity);
	}

	public List<Cliente> findAll() {
		return this.clienteRepository.findAll();
	}

	public Cliente findOne(final String id) {
		return this.clienteRepository.findOne(id);
	}
	
	public Cliente findBy(final Cliente cliente) {
		List<Cliente> clientes = this.clienteRepository.findAll(CarteiraSpecification.findBy(cliente));
		return clientes.isEmpty() ? null : clientes.iterator().next();
	}
	
	public Cliente findBy(String codigoCliente) {
		return this.clienteRepository.findByCodigo(codigoCliente);
	}

	public void delete(final Cliente entity) {
		this.clienteRepository.delete(entity);
	}

	public void delete(final Iterable<? extends Cliente> entities) {
		this.clienteRepository.delete(entities);
	}

	public Cliente getOneBy(final String codigo) {
		List<Cliente> clientes = this.clienteRepository.findAll(CarteiraSpecification.findBy(codigo));
		return clientes.isEmpty() ? null : clientes.iterator().next();
	}

	public List<Cliente> carrega(List<Cliente> carteiras) {
		List<Cliente> carteirasEncontradas = Lists.newArrayList();
		for (Cliente carteira : carteiras) {
			Cliente carteiraEncontrado = getOneBy(carteira.getCodigo());
			if (carteiraEncontrado == null) {
				carteirasEncontradas.add(carteira);
			}else {
				carteirasEncontradas.add(carteiraEncontrado);
			}
		}
		return carteirasEncontradas;
	}
}
