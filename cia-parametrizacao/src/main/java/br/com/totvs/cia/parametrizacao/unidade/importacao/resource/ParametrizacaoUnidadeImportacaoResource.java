package br.com.totvs.cia.parametrizacao.unidade.importacao.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.totvs.cia.parametrizacao.unidade.importacao.converter.ParametrizacaoUnidadeImportacaoConverter;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ParametrizacaoUnidadeImportacaoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.AbstractParametrizacaoUnidadeImportacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/parametrizacao/unidadeimportacao")
@Api(value = "ParametrizacaoUnidadeImportacao")
public class ParametrizacaoUnidadeImportacaoResource {

	@Autowired
	private ParametrizacaoUnidadeImportacaoService service;

	@Autowired
	private ParametrizacaoUnidadeImportacaoConverter converter;

	@ApiOperation(value = "Serviço de consulta das Parametrizações de Unidade de Importação por layout")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/layout", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ParametrizacaoUnidadeImportacaoJson>> listBy(
			@ApiParam(required = false) @RequestParam(required = false) final String layout) {

		final List<AbstractParametrizacaoUnidadeImportacao> models = this.service
				.listByLayout(layout);

		final List<ParametrizacaoUnidadeImportacaoJson> jsons = this.converter
				.convertListModelFrom(models);

		return new ResponseEntity<List<ParametrizacaoUnidadeImportacaoJson>>(jsons, HttpStatus.OK);
	}
}