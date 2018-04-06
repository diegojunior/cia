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

import br.com.totvs.cia.parametrizacao.layout.converter.LayoutXmlConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutXmlJson;
import br.com.totvs.cia.parametrizacao.layout.json.StatusLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutXmlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/parametrizacao/layout/xml")
@Api(value = "Layout Posicional")
public class LayoutXmlResource {

	@Autowired
	private LayoutXmlService layoutXmlService;

	@Autowired
	private LayoutXmlConverter layoutXmlConverter;

	@ApiOperation(value = "Serviço de inclusão do Layout XML")
	@RequestMapping(method = RequestMethod.POST, path = "adicionar", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutXmlJson> adicionar(@RequestBody final LayoutXmlJson layoutXml) {
		layoutXml.setTipoLayout(TipoLayoutEnumJson.XML);
		LayoutXml layout = this.layoutXmlConverter.convertFrom(layoutXml);
		this.layoutXmlService.save(layout);

		return new ResponseEntity<LayoutXmlJson>(HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de consulta de Layout por codigo")
	@RequestMapping(method = RequestMethod.GET, path = "list", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<LayoutXmlJson>> search(@RequestParam(required = false) final String codigo,
			@RequestParam(required = false) final String descricao, final StatusLayoutEnumJson status) {
		
		StatusLayoutEnum statusModel = status != null ? StatusLayoutEnum.fromCodigo(status.getCodigo()) : null;

		List<LayoutXml> layouts = this.layoutXmlService.findAll(codigo, descricao, statusModel, TipoLayoutEnum.XML);

		List<LayoutXmlJson> layoutsJson = this.layoutXmlConverter.convertListModelFrom(layouts);

		return new ResponseEntity<List<LayoutXmlJson>>(layoutsJson, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de alteração do Layout XML")
	@RequestMapping(method = RequestMethod.PUT, path = "/alterar", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutXmlJson> alterar(@RequestBody final LayoutXmlJson layoutJson) {

		LayoutXml layoutConvertido = this.layoutXmlConverter.convertFrom(layoutJson);

		LayoutXml layoutAlterado = this.layoutXmlService.update(layoutConvertido);

		LayoutXmlJson layout = this.layoutXmlConverter.convertFrom(layoutAlterado);

		return new ResponseEntity<LayoutXmlJson>(layout, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço para Ativar um Layout XML")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/ativar", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutXmlJson> ativar(@RequestBody final String id) {
		
		LayoutXml layoutAtivado = this.layoutXmlService.ativar(id);

		return new ResponseEntity<LayoutXmlJson>(this.layoutXmlConverter.convertFrom(layoutAtivado), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço para Inativar um Layout XML")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/inativar", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutXmlJson> inativar(@RequestBody final String id) {
		
		LayoutXml layoutInativo = this.layoutXmlService.inativar(id);

		return new ResponseEntity<LayoutXmlJson>(this.layoutXmlConverter.convertFrom(layoutInativo), HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de remoção do Layout XML")
	@RequestMapping(method = RequestMethod.DELETE, path = "/delete", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@RequestBody final List<LayoutXmlJson> layoutXml) {
		this.layoutXmlService.delete(this.layoutXmlConverter.convertListJsonFrom(layoutXml));
	}

	@ApiOperation(value = "Serviço de remoção de todos os Layouts")
	@RequestMapping(method = RequestMethod.DELETE, path = "/delete/all")
	public void deleteAll() {
		this.layoutXmlService.deleteAll();
	}

}
