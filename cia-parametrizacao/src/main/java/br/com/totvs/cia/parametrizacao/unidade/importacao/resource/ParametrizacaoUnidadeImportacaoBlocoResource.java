package br.com.totvs.cia.parametrizacao.unidade.importacao.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.totvs.cia.parametrizacao.unidade.importacao.converter.ParametrizacaoUnidadeImportacaoBlocoConverter;
import br.com.totvs.cia.parametrizacao.unidade.importacao.converter.ParametrizacaoUnidadeImportacaoBlocoResumidoConverter;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ParametrizacaoUnidadeImportacaoBlocoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ParametrizacaoUnidadeImportacaoBlocoResumidoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoBlocoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/parametrizacao/unidadeimportacao/bloco")
@Api(value = "ParametrizacaoUnidadeImportacaoBloco")
public class ParametrizacaoUnidadeImportacaoBlocoResource {

	@Autowired
	private ParametrizacaoUnidadeImportacaoBlocoService parametrizacaoUnidadeImportacaoService;

	@Autowired
	private ParametrizacaoUnidadeImportacaoBlocoConverter parametrizacaoUnidadeImportacaoConverter;

	@Autowired
	private ParametrizacaoUnidadeImportacaoBlocoResumidoConverter parametrizacaoUnidadeImportacaoResumidoConverter;

	@ApiOperation(value = "Serviço de consulta das Parametrizações de Unidade de Importação de Blocos por código da unidade, descrição, e código do layout")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/search", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ParametrizacaoUnidadeImportacaoBlocoJson>> search(
			@ApiParam(required = false) @RequestParam(required = false) final String codigo,
			@ApiParam(required = false) @RequestParam(required = false) final String descricao,
			@ApiParam(required = false) @RequestParam(required = false) final String codigoLayout) {		
		
		final List<ParametrizacaoUnidadeImportacaoBloco> parametrizacoesUnidadeImportacao = this.parametrizacaoUnidadeImportacaoService
				.listBy(codigo, descricao, codigoLayout);

		final List<ParametrizacaoUnidadeImportacaoBlocoJson> jsons = this.parametrizacaoUnidadeImportacaoConverter
				.convertListModelFrom(parametrizacoesUnidadeImportacao);

		return new ResponseEntity<List<ParametrizacaoUnidadeImportacaoBlocoJson>>(jsons, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de consulta das Parametrizações de Unidade de Importação por layout")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/layout", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ParametrizacaoUnidadeImportacaoBlocoResumidoJson>> getBy(
			@ApiParam(required = false) @RequestParam(required = false) final String layout) {

		final List<ParametrizacaoUnidadeImportacaoBloco> models = this.parametrizacaoUnidadeImportacaoService.listByLayout(layout);

		final List<ParametrizacaoUnidadeImportacaoBlocoResumidoJson> jsons = this.parametrizacaoUnidadeImportacaoResumidoConverter.convertListModelFrom(models);

		return new ResponseEntity<List<ParametrizacaoUnidadeImportacaoBlocoResumidoJson>>(jsons, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de inclusão de Parametrização de Unidade de Importação")
	@RequestMapping(method = RequestMethod.POST, 
					path = "/incluir", 
					consumes = {MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ParametrizacaoUnidadeImportacaoBlocoJson> incluir(
			@RequestBody final ParametrizacaoUnidadeImportacaoBlocoJson json) {
		
		final ParametrizacaoUnidadeImportacaoBloco model = this.parametrizacaoUnidadeImportacaoConverter.convertFrom(json);
		
		final ParametrizacaoUnidadeImportacaoBloco novaParametrizacao = this.parametrizacaoUnidadeImportacaoService
				.incluir(model);

		final ParametrizacaoUnidadeImportacaoBlocoJson parametrizacaoJson = this.parametrizacaoUnidadeImportacaoConverter
				.convertFrom(novaParametrizacao);

		return new ResponseEntity<ParametrizacaoUnidadeImportacaoBlocoJson>(parametrizacaoJson, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de Alteração de Parametrização de Unidade de Importação")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/alterar", 
					consumes = {MediaType.APPLICATION_JSON_VALUE}, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ParametrizacaoUnidadeImportacaoBlocoJson> alterar(
			@RequestBody final ParametrizacaoUnidadeImportacaoBlocoJson json) {

		final ParametrizacaoUnidadeImportacaoBloco model = this.parametrizacaoUnidadeImportacaoConverter.convertFrom(json);

		final ParametrizacaoUnidadeImportacaoBloco novaParametrizacao = this.parametrizacaoUnidadeImportacaoService
				.alterar(model);

		final ParametrizacaoUnidadeImportacaoBlocoJson parametrizacaoJson = this.parametrizacaoUnidadeImportacaoConverter
				.convertFrom(novaParametrizacao);

		return new ResponseEntity<ParametrizacaoUnidadeImportacaoBlocoJson>(parametrizacaoJson, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de remoção de Parametrização de Unidade de Importação")
	@RequestMapping(method = RequestMethod.DELETE, 
					path = "/delete", 
					consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@RequestBody final List<ParametrizacaoUnidadeImportacaoBlocoResumidoJson> json) {

		final List<ParametrizacaoUnidadeImportacaoBloco> model = this.parametrizacaoUnidadeImportacaoResumidoConverter
				.convertListJsonFrom(json);

		this.parametrizacaoUnidadeImportacaoService.delete(model);
	}

	@ApiOperation(value = "Serviço de remoção de todas as Parametrizações de Unidade de Importação")
	@RequestMapping(method = RequestMethod.DELETE, 
					path = "/delete/all")
	public void deleteAll() {
		this.parametrizacaoUnidadeImportacaoService.deleteAll();
	}
}