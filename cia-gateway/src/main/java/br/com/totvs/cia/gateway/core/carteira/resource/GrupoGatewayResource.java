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
import br.com.totvs.cia.cadastro.grupo.json.GrupoJson;
import br.com.totvs.cia.gateway.core.carteira.service.GrupoGatewayService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/gateway/grupo")
@Api(value = "Grupos via Integração")
public class GrupoGatewayResource {

	@Autowired
	private GrupoGatewayService grupoCarteiraService;
	
	@ApiOperation(value = "Serviço de consulta de Grupos de Carteiras por código via Integração")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/search",					
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<GrupoJson>> search(
			@RequestParam final SistemaJsonEnum sistema, 
			@RequestParam final String codigo,
			@RequestParam final Integer pagina,
			@RequestParam final Integer tamanhoPagina) {
		
		SistemaEnum sistemaEnum = SistemaEnum.fromCodigo(sistema.getCodigo());

		List<GrupoJson> grupos = this.grupoCarteiraService.search(sistemaEnum, codigo, pagina, tamanhoPagina);

		return new ResponseEntity<List<GrupoJson>>(grupos, HttpStatus.OK);
	}
}