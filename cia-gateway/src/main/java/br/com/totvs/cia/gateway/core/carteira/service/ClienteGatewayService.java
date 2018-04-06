package br.com.totvs.cia.gateway.core.carteira.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.carteira.json.ClienteJson;
import br.com.totvs.cia.gateway.amplis.carteira.service.CarteiraAmplisService;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class ClienteGatewayService {

	@Autowired
	private CarteiraAmplisService carteiraAmplisService;
	
	public List<ClienteJson> getAll(final SistemaEnum sistema){
		try {
			List<ClienteJson> carteiras = Lists.newArrayList();
			if(sistema.isAmplis()){
				carteiras.addAll(this.carteiraAmplisService.getAll());
				ordenarCrescentePorCodigo(carteiras);
				return carteiras;
			}
			throw new RuntimeException("Sistema não identificado");
		} catch (Exception e) {
			throw new GatewayException("Clientes não encontradas.");
		}
	}
	
	public List<ClienteJson> search(final SistemaEnum sistema, final String codigo, Integer pagina, Integer tamanhoPagina){
		List<ClienteJson> carteiras = Lists.newArrayList();
		try {
			if(sistema.isAmplis()){
				carteiras.addAll(this.carteiraAmplisService.search(codigo, pagina, tamanhoPagina));
				ordenarCrescentePorCodigo(carteiras);
				return carteiras;
			}
			throw new RuntimeException("Sistema não identificado");
		} catch (Exception e) {
			throw new GatewayException("Carteiras não encontradas.");
		}
	}
	
	private void ordenarCrescentePorCodigo(List<ClienteJson> carteiras) {
		if(carteiras != null && !carteiras.isEmpty()){
			Collections.sort(carteiras, new Comparator<ClienteJson>() {
				@Override
				public int compare(ClienteJson c1, ClienteJson c2) {
					return c1.getCodigo().compareToIgnoreCase(c2.getCodigo());
				}
			});
		}
	}
}