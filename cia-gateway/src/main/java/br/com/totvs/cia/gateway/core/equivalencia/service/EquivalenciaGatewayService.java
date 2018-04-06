package br.com.totvs.cia.gateway.core.equivalencia.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.json.EquivalenciaJson;
import br.com.totvs.cia.gateway.amplis.equivalencia.service.EquivalenciaAmplisService;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class EquivalenciaGatewayService {
	
	private static final Logger LOG = Logger.getLogger(EquivalenciaGatewayService.class);

	@Autowired
	private EquivalenciaAmplisService service;

	public List<EquivalenciaJson> getEquivalenciasBy(final SistemaEnum sistema, final String senderMnemonic,
			final String codigoTipoEquivalencia) {
		
		LOG.debug("Obtendo as equivalências via integração gateway");
		
		try {

			if (sistema.isAmplis()) {
				
				return this.service.getEquivalenciaBy(senderMnemonic, codigoTipoEquivalencia);
				
			}

			throw new RuntimeException("Sistema não identificado");

		} catch (final Exception e) {
			LOG.error("Houve um erro ao buscar as equivalências via integração gateway", e);
			throw new GatewayException("Equivalências não encontradas.");

		}

	}

}
