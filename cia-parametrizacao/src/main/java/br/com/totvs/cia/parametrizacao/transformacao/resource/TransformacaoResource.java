package br.com.totvs.cia.parametrizacao.transformacao.resource;

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
import br.com.totvs.cia.parametrizacao.transformacao.converter.TransformacaoConverter;
import br.com.totvs.cia.parametrizacao.transformacao.json.TipoTransformacaoEnumJson;
import br.com.totvs.cia.parametrizacao.transformacao.json.TransformacaoJson;
import br.com.totvs.cia.parametrizacao.transformacao.model.TipoTransformacaoEnum;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api/v1/parametrizacao/transformacao")
@Api(value = "Transformacao")
public class TransformacaoResource {

	@Autowired
	private TransformacaoService transformacaoService;

	@Autowired
	private TransformacaoConverter transformacaoConverter;

	@ApiOperation(value = "Serviço de consulta de Transformacoes por código")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/search", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<TransformacaoJson>> search(
			@ApiParam(required = false) @RequestParam(required = false) final TipoLayoutEnumJson tipoLayout,
			@ApiParam(required = false) @RequestParam(required = false) final String layout,
			@ApiParam(required = false) @RequestParam(required = false) final String sessao,
			@ApiParam(required = false) @RequestParam(required = false) final String campo,
			@ApiParam(required = false) @RequestParam(required = false) final TipoTransformacaoEnumJson tipoTransformacao) {
		
		final TipoLayoutEnum tipoLayoutEnum = tipoLayout != null ? TipoLayoutEnum.fromCodigo(tipoLayout.getCodigo()): null;
		
		final TipoTransformacaoEnum tipoTransformacaoEnum = tipoTransformacao != null ? TipoTransformacaoEnum.fromCodigo(tipoTransformacao.getCodigo()) : null;

		final List<Transformacao> transformacoes = this.transformacaoService.getByFilters(tipoLayoutEnum, layout, sessao, campo, tipoTransformacaoEnum);

		final List<TransformacaoJson> jsons = this.transformacaoConverter.convertListModelFrom(transformacoes);
		
		return new ResponseEntity<List<TransformacaoJson>>(jsons, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço de inclusão de uma Transformação")
	@RequestMapping(method = RequestMethod.POST, 
					path = "/incluir", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TransformacaoJson> incluir(@RequestBody final TransformacaoJson json) {
		
		final Transformacao model = this.transformacaoConverter.convertFrom(json);

		final Transformacao novoTransformacao = this.transformacaoService.incluir(model);
		
		final TransformacaoJson transformacaoJson = this.transformacaoConverter.convertFrom(novoTransformacao);

		return new ResponseEntity<TransformacaoJson>(transformacaoJson, HttpStatus.OK);

	}

	@ApiOperation(value = "Serviço de remoção de Transformações")
	@RequestMapping(method = RequestMethod.DELETE, 
					path = "/delete", 
					consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@RequestBody final List<TransformacaoJson> json) {
		
		final List<Transformacao> model = this.transformacaoConverter.convertListJsonFrom(json);
		
		this.transformacaoService.delete(model);
	}
}