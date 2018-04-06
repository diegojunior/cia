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
import br.com.totvs.cia.cadastro.equivalencia.json.TipoEquivalenciaJson;
import br.com.totvs.cia.gateway.core.equivalencia.service.TipoEquivalenciaGatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/gateway/equivalencia/tipo")
@Api(value = "Tipo de Equivalência via Integração")
public class TipoEquivalenciaGatewayResource {

	@Autowired
	private TipoEquivalenciaGatewayService tipoEquivalenciaService;

	@ApiOperation(value = "Serviço de consulta de Tipos de Equivalência via Integração")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/all",					
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<TipoEquivalenciaJson>> getAll(@RequestParam final SistemaJsonEnum sistema) throws Exception{
		
		SistemaEnum sistemaEnum = SistemaEnum.fromCodigo(sistema.getCodigo());

		List<TipoEquivalenciaJson> clientes = this.tipoEquivalenciaService.getAll(sistemaEnum);

		return new ResponseEntity<List<TipoEquivalenciaJson>>(clientes, HttpStatus.OK);
	}
}