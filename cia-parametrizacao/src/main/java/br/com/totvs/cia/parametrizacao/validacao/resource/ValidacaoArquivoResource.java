package br.com.totvs.cia.parametrizacao.validacao.resource;

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
import br.com.totvs.cia.parametrizacao.validacao.converter.ValidacaoArquivoConverter;
import br.com.totvs.cia.parametrizacao.validacao.json.CampoValidacaoArquivoEnumJson;
import br.com.totvs.cia.parametrizacao.validacao.json.LocalValidacaoArquivoEnumJson;
import br.com.totvs.cia.parametrizacao.validacao.json.ValidacaoArquivoJson;
import br.com.totvs.cia.parametrizacao.validacao.model.AbstractValidacaoArquivo;
import br.com.totvs.cia.parametrizacao.validacao.model.CampoValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.LocalValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.TipoValidacaoEnum;
import br.com.totvs.cia.parametrizacao.validacao.service.ValidacaoArquivoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/parametrizacao/validacao/arquivo")
@Api(value = "ValidacaoArquivo")
public class ValidacaoArquivoResource {

	@Autowired
	private ValidacaoArquivoService validacaoService;

	@Autowired
	private ValidacaoArquivoConverter validacaoConverter;

	@ApiOperation(value = "Serviço de consulta de Validacoes de Arquivo")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/search", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ValidacaoArquivoJson>> search(
			@ApiParam(required = false) @RequestParam(required = false) final TipoLayoutEnumJson tipoLayout,
			@ApiParam(required = false) @RequestParam(required = false) final String layout,
			@ApiParam(required = false) @RequestParam(required = false) final CampoValidacaoArquivoEnumJson campoValidacao,
			@ApiParam(required = false) @RequestParam(required = false) final LocalValidacaoArquivoEnumJson localValidacao) {
		
		TipoValidacaoEnum tipoValidacaoModel = TipoValidacaoEnum.ARQUIVO;
		
		TipoLayoutEnum tipoLayoutModel = tipoLayout != null ? TipoLayoutEnum.fromCodigo(tipoLayout.getCodigo()) : null;
		CampoValidacaoArquivoEnum campoValidacaoModel = campoValidacao != null ? CampoValidacaoArquivoEnum.fromCodigo(campoValidacao.getCodigo()) : null;
		LocalValidacaoArquivoEnum localValidacaoModel = localValidacao != null ? LocalValidacaoArquivoEnum.fromCodigo(localValidacao.getCodigo()) : null;
		
		List<AbstractValidacaoArquivo> validacoes = this.validacaoService.search(tipoLayoutModel, layout, 
				tipoValidacaoModel, campoValidacaoModel, localValidacaoModel);
		
		List<ValidacaoArquivoJson> json = this.validacaoConverter.convertListModelFrom(validacoes);
		
		return new ResponseEntity<List<ValidacaoArquivoJson>>(json, HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "Serviço de inclusão de uma Validação de Arquivo")
	@RequestMapping(method = RequestMethod.POST, 
					path = "/incluir", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ValidacaoArquivoJson> incluir(@RequestBody final ValidacaoArquivoJson json) {
		
		AbstractValidacaoArquivo model = this.validacaoConverter.convertFrom(json);

		AbstractValidacaoArquivo novoValidacao = this.validacaoService.incluir(model);
		
		ValidacaoArquivoJson validacaoJson = this.validacaoConverter.convertFrom(novoValidacao);
		
		return new ResponseEntity<ValidacaoArquivoJson>(validacaoJson, HttpStatus.OK);

	}

	@ApiOperation(value = "Serviço de Alteração de uma Validação de Arquivo")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/alterar", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ValidacaoArquivoJson> alterar(@RequestBody final ValidacaoArquivoJson json) {
		
		AbstractValidacaoArquivo model = this.validacaoConverter.convertFrom(json);

		AbstractValidacaoArquivo novoValidacao = this.validacaoService.alterar(model);
		
		ValidacaoArquivoJson validacaoJson = this.validacaoConverter.convertFrom(novoValidacao);

		return new ResponseEntity<ValidacaoArquivoJson>(validacaoJson, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de remoção de Validações de Arquivo")
	@RequestMapping(method = RequestMethod.DELETE, 
					path = "/delete", 
					consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@RequestBody final List<ValidacaoArquivoJson> json) {
		
		List<AbstractValidacaoArquivo> model = this.validacaoConverter.convertListJsonFrom(json);
		
		this.validacaoService.delete(model);
	}
}