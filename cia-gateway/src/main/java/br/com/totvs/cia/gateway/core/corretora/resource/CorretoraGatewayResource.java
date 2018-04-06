package br.com.totvs.cia.gateway.core.corretora.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.totvs.cia.cadastro.agente.json.AgenteJson;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.gateway.core.corretora.service.CorretoraGatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/gateway/corretora")
@Api(value = "Corretoras via Integração")
public class CorretoraGatewayResource {

	@Autowired
	private CorretoraGatewayService corretoraService;

	@ApiOperation(value = "Serviço de consulta de Corretoras via Integração")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/all",
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<AgenteJson>> getAll(@RequestParam final SistemaJsonEnum sistema) throws Exception{
		
		SistemaEnum sistemaEnum = SistemaEnum.fromCodigo(sistema.getCodigo());

		List<AgenteJson> corretoras = this.corretoraService.getAll(sistemaEnum);

		return new ResponseEntity<List<AgenteJson>>(corretoras, HttpStatus.OK);
	}
}