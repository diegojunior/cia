package br.com.totvs.cia.parametrizacao.dominio.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.totvs.cia.cadastro.tipo.TipoValorDominioEnum;
import br.com.totvs.cia.parametrizacao.dominio.converter.DominioConverter;
import br.com.totvs.cia.parametrizacao.dominio.json.DominioJson;
import br.com.totvs.cia.parametrizacao.dominio.json.TipoValorDominioEnumJson;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.dominio.service.DominioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/parametrizacao/dominio")
@Api(value = "Dominio")
public class DominioResource {

	@Autowired
	private DominioService dominioService;

	@Autowired
	private DominioConverter dominioConverter;

	@ApiOperation(value = "Serviço de consulta de Dominios por código e id de Tipo de Dominio")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/list", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<DominioJson>> search(
			@ApiParam(required = false) @RequestParam(required = false) final String codigo,
			@ApiParam(required = false) @RequestParam(required = false) final TipoValorDominioEnumJson tipo) {

		final TipoValorDominioEnum tipoEnum = tipo != null ? TipoValorDominioEnum.fromCodigo(tipo.getCodigo()) : null;

		final List<Dominio> dominios = this.dominioService.getByFilters(codigo, tipoEnum);

		final List<DominioJson> jsons = this.dominioConverter.convertListModelFrom(dominios);
		
		return new ResponseEntity<List<DominioJson>>(jsons, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço de consulta de Dominios por código")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/{codigo}", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<DominioJson> getBy(
			@ApiParam(required = false) @PathVariable final String codigo) throws URISyntaxException {

		final Dominio dominio = this.dominioService.getBy(codigo);

		final DominioJson json = this.dominioConverter.convertFrom(dominio);
		
		return new ResponseEntity<DominioJson>(json, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço de inclusão de Dominio")
	@RequestMapping(method = RequestMethod.POST, 
					path = "/incluir", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<DominioJson> incluir(@RequestBody final DominioJson json) throws URISyntaxException {
		
		final Dominio model = this.dominioConverter.convertFrom(json);

		final Dominio novoDominio = this.dominioService.incluir(model);
		
		final DominioJson dominioJson = this.dominioConverter.convertFrom(novoDominio);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{codigo}")
				.buildAndExpand(dominioJson.getCodigo())
				.toUri();
		
		return ResponseEntity.created(location).body(dominioJson);

	}

	@ApiOperation(value = "Serviço de Alteração de Dominio")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/alterar", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<DominioJson> alterar(@RequestBody final DominioJson json) {
		
		final Dominio model = this.dominioConverter.convertFrom(json);

		final Dominio novoDominio = this.dominioService.alterar(model);
		
		final DominioJson dominioJson = this.dominioConverter.convertFrom(novoDominio);

		return new ResponseEntity<DominioJson>(dominioJson, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de remoção de Dominio")
	@RequestMapping(method = RequestMethod.DELETE, 
					path = "/delete", 
					consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@RequestBody final List<DominioJson> json) {
		
		final List<Dominio> model = this.dominioConverter.convertListJsonFrom(json);
		
		this.dominioService.delete(model);
	}

	@ApiOperation(value = "Serviço de remoção de todos os Dominios")
	@RequestMapping(method = RequestMethod.DELETE, 
					path = "/delete/all")
	public void deleteAll() {
		this.dominioService.deleteAll();
	}
}