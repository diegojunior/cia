package br.com.totvs.cia.gateway.core.equivalencia.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.json.EquivalenciaJson;
import br.com.totvs.cia.cadastro.equivalencia.json.TipoEquivalenciaJsonEnum;
import br.com.totvs.cia.gateway.core.equivalencia.service.EquivalenciaGatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/gateway/equivalencia")
@Api("Equivalência via Integração")
public class EquivalenciaGatewayResource {

	@Autowired
	private EquivalenciaGatewayService service;
	
	@RequestMapping(method = RequestMethod.GET,
			path = "/sistema/{sistema}/tipoEquivalencia/{tipoEquivalencia}",
			produces = {MediaType.APPLICATION_JSON_VALUE})
	@ApiOperation(value = "Serviço de consulta da Equivalência do sistema via Integração pelo Destinatario e Tipo de Equivalência")
	public ResponseEntity<List<EquivalenciaJson>> getBy(
			@PathVariable("sistema") final SistemaJsonEnum sistemaJson,
			@PathVariable("tipoEquivalencia") final TipoEquivalenciaJsonEnum tipoEquivalencia,
			@RequestParam("sender") final String senderMnemonic) {
		
		final SistemaEnum sistema = SistemaEnum.fromCodigo(sistemaJson.getCodigo());
		
		final List<EquivalenciaJson> equivalencias = this.service.getEquivalenciasBy(sistema, senderMnemonic, tipoEquivalencia.getCodigo());
		
		return ResponseEntity.ok(equivalencias);
		
	}
	
}
