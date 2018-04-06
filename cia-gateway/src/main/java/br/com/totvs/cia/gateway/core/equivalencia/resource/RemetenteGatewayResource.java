package br.com.totvs.cia.gateway.core.equivalencia.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.json.RemetenteJson;
import br.com.totvs.cia.gateway.core.equivalencia.service.RemetenteGatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/gateway/equivalencia/remetente")
@Api(value = "Remetente via Integração")
public class RemetenteGatewayResource {

	@Autowired
	private RemetenteGatewayService remetenteService;

	@ApiOperation(value = "Serviço de consulta de Remetentes via Integração")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/all",					
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<RemetenteJson>> getAll(@RequestParam final SistemaJsonEnum sistema) throws Exception{
		
		SistemaEnum sistemaEnum = SistemaEnum.fromCodigo(sistema.getCodigo());

		List<RemetenteJson> clientes = this.remetenteService.getAll(sistemaEnum);

		return new ResponseEntity<List<RemetenteJson>>(clientes, HttpStatus.OK);
	}
}