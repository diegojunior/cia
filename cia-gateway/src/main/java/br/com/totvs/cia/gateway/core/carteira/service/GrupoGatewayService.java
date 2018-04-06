package br.com.totvs.cia.gateway.core.carteira.service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.grupo.json.GrupoJson;
import br.com.totvs.cia.gateway.amplis.carteira.service.GrupoCarteiraAmplisService;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class GrupoGatewayService {

	@Autowired
	private GrupoCarteiraAmplisService grupoCarteiraAmplisService;
	
	public List<GrupoJson> search(final SistemaEnum sistema, final String codigo, Integer pagina, Integer tamanhoPagina){
		List<GrupoJson> grupos = Lists.newArrayList();
		try {
			if(sistema.isAmplis()){
				grupos.addAll(this.grupoCarteiraAmplisService.search(codigo, pagina, tamanhoPagina));
				ordenarCrescentePorCodigo(grupos);
				return grupos;
			}
			throw new RuntimeException("Sistema não identificado");
		} catch (Exception e) {
			throw new GatewayException("Grupos de Carteiras não encontrados.");
		}
	}
	
	private void ordenarCrescentePorCodigo(List<GrupoJson> grupos) {
		if(grupos != null && !grupos.isEmpty()){
			Collections.sort(grupos, new Comparator<GrupoJson>() {
				@Override
				public int compare(GrupoJson c1, GrupoJson c2) {
					return c1.getCodigo().compareToIgnoreCase(c2.getCodigo());
				}
			});
		}
	}
}