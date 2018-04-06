package br.com.totvs.cia.gateway.core.carteira.resource;

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
import br.com.totvs.cia.cadastro.carteira.json.ClienteJson;
import br.com.totvs.cia.gateway.core.carteira.service.ClienteGatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/gateway/cliente")
@Api(value = "Clientes via Integração")
public class ClienteGatewayResource {

	@Autowired
	private ClienteGatewayService carteiraService;

	@ApiOperation(value = "Serviço de consulta de Carteiras via Integração")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/all",					
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ClienteJson>> getAll(@RequestParam final SistemaJsonEnum sistema) {
		
		SistemaEnum sistemaEnum = SistemaEnum.fromCodigo(sistema.getCodigo());

		List<ClienteJson> clientes = this.carteiraService.getAll(sistemaEnum);

		return new ResponseEntity<List<ClienteJson>>(clientes, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço de consulta de Carteiras por código via Integração")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/search",					
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ClienteJson>> search(
			@RequestParam final SistemaJsonEnum sistema, 
			@RequestParam final String codigo,
			@RequestParam final Integer pagina,
			@RequestParam final Integer tamanhoPagina) {
		
		SistemaEnum sistemaEnum = SistemaEnum.fromCodigo(sistema.getCodigo());

		List<ClienteJson> clientes = this.carteiraService.search(sistemaEnum, codigo, pagina, tamanhoPagina);

		return new ResponseEntity<List<ClienteJson>>(clientes, HttpStatus.OK);
	}
}