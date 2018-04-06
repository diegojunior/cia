package br.com.totvs.cia.cadastro.carteira.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.carteira.json.ClienteJson;
import br.com.totvs.cia.cadastro.carteira.model.Cliente;
import br.com.totvs.cia.cadastro.carteira.repository.ClienteRepository;
import br.com.totvs.cia.infra.converter.JsonConverter;

@Component
public class ClienteConverter extends JsonConverter<Cliente, ClienteJson>{

	@Autowired
	private ClienteRepository clienteRepository;

	@Override
	public ClienteJson convertFrom(final Cliente model) {
		return new ClienteJson(model.getCodigo());
	}

	@Override
	public Cliente convertFrom(final ClienteJson json) {
		try{
			if (json.getId() != null) {
				return this.clienteRepository.getOne(json.getId());
			}

			return new Cliente(json);
		}catch (Exception ex){
			throw new RuntimeException(ex);
		}
	}
}
