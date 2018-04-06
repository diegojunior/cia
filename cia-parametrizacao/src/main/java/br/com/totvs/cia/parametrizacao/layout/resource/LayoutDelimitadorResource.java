package br.com.totvs.cia.parametrizacao.layout.resource;

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

import br.com.totvs.cia.parametrizacao.layout.converter.LayoutDelimitadorConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutDelimitadorJson;
import br.com.totvs.cia.parametrizacao.layout.json.StatusLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutDelimitadorService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/parametrizacao/layout/delimitador")
@Api(value = "Layout Delimitador")
public class LayoutDelimitadorResource {

	@Autowired
	private LayoutDelimitadorService layoutDelimitadorService;

	@Autowired
	private LayoutDelimitadorConverter layoutDelimitadorConverter;

	@ApiOperation(value = "Serviço de inclusão do Layout Delimitador")
	@RequestMapping(method = RequestMethod.POST, 
					path = "adicionar", consumes = {
					MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutDelimitadorJson> adicionar(@RequestBody final LayoutDelimitadorJson layoutDelimitador) {
		layoutDelimitador.setTipoLayout(TipoLayoutEnumJson.DELIMITADOR);
		LayoutDelimitador layout = this.layoutDelimitadorConverter.convertFrom(layoutDelimitador);
		this.layoutDelimitadorService.save(layout);

		return new ResponseEntity<LayoutDelimitadorJson>(HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de consulta de Layout por codigo, descrição, código identificador da sessão e status do layout")
	@RequestMapping(method = RequestMethod.GET, path = "list", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<LayoutDelimitadorJson>> list(@RequestParam(required = false) final String codigo,
			@RequestParam(required = false) final String descricao,
			@RequestParam(required = false) final String codigoIdentificador,
			@RequestParam(required = false) final StatusLayoutEnumJson status) {

		final StatusLayoutEnum statusFilter = status != null ? StatusLayoutEnum.fromCodigo(status.getCodigo()) : null;

		List<LayoutDelimitador> layouts = this.layoutDelimitadorService.findAll(codigo, descricao, codigoIdentificador,
				statusFilter);

		List<LayoutDelimitadorJson> layoutsJson = this.layoutDelimitadorConverter.convertListModelFrom(layouts);

		return new ResponseEntity<List<LayoutDelimitadorJson>>(layoutsJson, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de alteração do Layout Delimitador")
	@RequestMapping(method = RequestMethod.PUT, path = "/alterar", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutDelimitadorJson> alterar(@RequestBody final LayoutDelimitadorJson layoutJson) {

		LayoutDelimitador layoutConvertido = this.layoutDelimitadorConverter.convertFrom(layoutJson);

		LayoutDelimitador layoutAlterado = this.layoutDelimitadorService.update(layoutConvertido);

		LayoutDelimitadorJson layout = this.layoutDelimitadorConverter.convertFrom(layoutAlterado);

		return new ResponseEntity<LayoutDelimitadorJson>(layout, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço para Ativar um Layout Delimitador")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/ativar", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutDelimitadorJson> ativar(@RequestBody final String id) {
		
		LayoutDelimitador layoutAtivado = this.layoutDelimitadorService.ativar(id);

		return new ResponseEntity<LayoutDelimitadorJson>(this.layoutDelimitadorConverter.convertFrom(layoutAtivado), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço para Inativar um Layout Delimitador")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/inativar", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutDelimitadorJson> inativar(@RequestBody final String id) {
		
		LayoutDelimitador layoutInativo = this.layoutDelimitadorService.inativar(id);

		return new ResponseEntity<LayoutDelimitadorJson>(this.layoutDelimitadorConverter.convertFrom(layoutInativo), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço de remoção do Layout Delimitador")
	@RequestMapping(method = RequestMethod.DELETE, path = "/delete", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@RequestBody final List<LayoutDelimitadorJson> layoutDelimitador) {
		this.layoutDelimitadorService.delete(this.layoutDelimitadorConverter.convertListJsonFrom(layoutDelimitador));
	}

	@ApiOperation(value = "Serviço de remoção de todos os Layouts")
	@RequestMapping(method = RequestMethod.DELETE, path = "/delete/all")
	public void deleteAll() {
		this.layoutDelimitadorService.deleteAll();
	}
}