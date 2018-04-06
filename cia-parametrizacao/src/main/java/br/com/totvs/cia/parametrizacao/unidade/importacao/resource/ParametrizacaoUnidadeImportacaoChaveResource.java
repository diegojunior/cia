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

import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.unidade.importacao.converter.ParametrizacaoUnidadeImportacaoChaveConverter;
import br.com.totvs.cia.parametrizacao.unidade.importacao.converter.ParametrizacaoUnidadeImportacaoChaveResumidoConverter;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ParametrizacaoUnidadeImportacaoChaveJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.json.ParametrizacaoUnidadeImportacaoChaveResumidoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoChaveService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/parametrizacao/unidadeimportacao/chave")
@Api(value = "ParametrizacaoUnidadeImportacaoChave")
public class ParametrizacaoUnidadeImportacaoChaveResource {

	@Autowired
	private ParametrizacaoUnidadeImportacaoChaveService parametrizacaoUnidadeImportacaoService;

	@Autowired
	private ParametrizacaoUnidadeImportacaoChaveConverter parametrizacaoUnidadeImportacaoConverter;

	@Autowired
	private ParametrizacaoUnidadeImportacaoChaveResumidoConverter parametrizacaoUnidadeImportacaoResumidoConverter;

	@ApiOperation(value = "Serviço de consulta das Parametrizações de Unidade de Importação por código da unidade, descrição, tipo de layout, código do layout e código identificador da unidade")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/search", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ParametrizacaoUnidadeImportacaoChaveJson>> search(
			@ApiParam(required = false) @RequestParam(required = false) final String codigo,
			@ApiParam(required = false) @RequestParam(required = false) final String descricao,
			@ApiParam(required = false) @RequestParam(required = false) final TipoLayoutEnumJson tipoLayout,
			@ApiParam(required = false) @RequestParam(required = false) final String codigoLayout,
			@ApiParam(required = false) @RequestParam(required = false) final String codigoIdentificador) {

		final TipoLayoutEnum tipoLayoutParam = tipoLayout != null ? TipoLayoutEnum.fromCodigo(tipoLayout.getCodigo()) : null;
		
		final List<ParametrizacaoUnidadeImportacaoChave> parametrizacoesUnidadeImportacao = this.parametrizacaoUnidadeImportacaoService
				.getBy(codigo, descricao, tipoLayoutParam, codigoLayout, codigoIdentificador);

		final List<ParametrizacaoUnidadeImportacaoChaveJson> jsons = this.parametrizacaoUnidadeImportacaoConverter
				.convertListModelFrom(parametrizacoesUnidadeImportacao);

		return new ResponseEntity<List<ParametrizacaoUnidadeImportacaoChaveJson>>(jsons, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de consulta das Parametrizações de Unidade de Importação por layout")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/layout", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ParametrizacaoUnidadeImportacaoChaveResumidoJson>> getBy(
			@ApiParam(required = false) @RequestParam(required = false) final String layout) {

		final List<ParametrizacaoUnidadeImportacaoChave> parametrizacoesUnidadeImportacao = this.parametrizacaoUnidadeImportacaoService
				.getByLayout(layout);

		final List<ParametrizacaoUnidadeImportacaoChaveResumidoJson> jsons = this.parametrizacaoUnidadeImportacaoResumidoConverter
				.convertListModelFrom(parametrizacoesUnidadeImportacao);

		return new ResponseEntity<List<ParametrizacaoUnidadeImportacaoChaveResumidoJson>>(jsons, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de inclusão de Parametrização de Unidade de Importação")
	@RequestMapping(method = RequestMethod.POST, 
					path = "/incluir", 
					consumes = {MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ParametrizacaoUnidadeImportacaoChaveJson> incluir(
			@RequestBody final ParametrizacaoUnidadeImportacaoChaveJson json) {
		
		final ParametrizacaoUnidadeImportacaoChave model = this.parametrizacaoUnidadeImportacaoConverter.convertFrom(json);
		
		this.parametrizacaoUnidadeImportacaoService.verificarUnidadeDuplicada(model);

		final ParametrizacaoUnidadeImportacaoChave novaParametrizacao = this.parametrizacaoUnidadeImportacaoService
				.incluir(model);

		final ParametrizacaoUnidadeImportacaoChaveJson dominioJson = this.parametrizacaoUnidadeImportacaoConverter
				.convertFrom(novaParametrizacao);

		return new ResponseEntity<ParametrizacaoUnidadeImportacaoChaveJson>(dominioJson, HttpStatus.OK);

	}

	@ApiOperation(value = "Serviço de Alteração de Parametrização de Unidade de Importação")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/alterar", 
					consumes = {MediaType.APPLICATION_JSON_VALUE}, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ParametrizacaoUnidadeImportacaoChaveJson> alterar(
			@RequestBody final ParametrizacaoUnidadeImportacaoChaveJson json) {

		final ParametrizacaoUnidadeImportacaoChave model = this.parametrizacaoUnidadeImportacaoConverter.convertFrom(json);

		final ParametrizacaoUnidadeImportacaoChave novaParametrizacao = this.parametrizacaoUnidadeImportacaoService
				.alterar(model);

		final ParametrizacaoUnidadeImportacaoChaveJson parametrizacaoJson = this.parametrizacaoUnidadeImportacaoConverter
				.convertFrom(novaParametrizacao);

		return new ResponseEntity<ParametrizacaoUnidadeImportacaoChaveJson>(parametrizacaoJson, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de remoção de Parametrização de Unidade de Importação")
	@RequestMapping(method = RequestMethod.DELETE, 
					path = "/delete", 
					consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@RequestBody final List<ParametrizacaoUnidadeImportacaoChaveJson> json) {

		final List<ParametrizacaoUnidadeImportacaoChave> model = this.parametrizacaoUnidadeImportacaoConverter
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