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

import br.com.totvs.cia.parametrizacao.layout.converter.LayoutPosicionalConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutPosicionalJson;
import br.com.totvs.cia.parametrizacao.layout.json.StatusLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutPosicionalService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/parametrizacao/layout/posicional")
@Api(value = "Layout Posicional")
public class LayoutPosicionalResource {

	@Autowired
	private LayoutPosicionalService layoutPosicionalService;

	@Autowired
	private LayoutPosicionalConverter layoutPosicionalConverter;

	@ApiOperation(value = "Serviço de inclusão do Layout Posicional")
	@RequestMapping(method = RequestMethod.POST, 
					path = "adicionar", consumes = {
					MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutPosicionalJson> adicionar(@RequestBody final LayoutPosicionalJson layoutPosicional) {
		layoutPosicional.setTipoLayout(TipoLayoutEnumJson.POSICIONAL);
		LayoutPosicional layout = this.layoutPosicionalConverter.convertFrom(layoutPosicional);
		this.layoutPosicionalService.save(layout);

		return new ResponseEntity<LayoutPosicionalJson>(HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de consulta de Layout por codigo, descrição, código identificador da sessão e status do layout")
	@RequestMapping(method = RequestMethod.GET, path = "list", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<LayoutPosicionalJson>> list(@RequestParam(required = false) final String codigo,
			@RequestParam(required = false) final String descricao,
			@RequestParam(required = false) final String codigoIdentificador,
			@RequestParam(required = false) final StatusLayoutEnumJson status) {

		final StatusLayoutEnum statusFilter = status != null ? StatusLayoutEnum.fromCodigo(status.getCodigo()) : null;

		List<LayoutPosicional> layouts = this.layoutPosicionalService.findAll(codigo, descricao, codigoIdentificador,
				statusFilter);

		List<LayoutPosicionalJson> layoutsJson = this.layoutPosicionalConverter.convertListModelFrom(layouts);

		return new ResponseEntity<List<LayoutPosicionalJson>>(layoutsJson, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de alteração do Layout Posicional")
	@RequestMapping(method = RequestMethod.PUT, path = "/alterar", consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutPosicionalJson> alterar(@RequestBody final LayoutPosicionalJson layoutJson) {

		LayoutPosicional layoutConvertido = this.layoutPosicionalConverter.convertFrom(layoutJson);

		LayoutPosicional layoutAlterado = this.layoutPosicionalService.update(layoutConvertido);

		LayoutPosicionalJson layout = this.layoutPosicionalConverter.convertFrom(layoutAlterado);

		return new ResponseEntity<LayoutPosicionalJson>(layout, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço para Ativar um Layout Posicional")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/ativar", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutPosicionalJson> ativar(@RequestBody final String id) {
		
		LayoutPosicional layoutAtivado = this.layoutPosicionalService.ativar(id);

		return new ResponseEntity<LayoutPosicionalJson>(this.layoutPosicionalConverter.convertFrom(layoutAtivado), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço para Inativar um Layout Posicional")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/inativar", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<LayoutPosicionalJson> inativar(@RequestBody final String id) {
		
		LayoutPosicional layoutInativo = this.layoutPosicionalService.inativar(id);

		return new ResponseEntity<LayoutPosicionalJson>(this.layoutPosicionalConverter.convertFrom(layoutInativo), HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de remoção do Layout Posicional")
	@RequestMapping(method = RequestMethod.DELETE, path = "/delete", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@RequestBody final List<LayoutPosicionalJson> layoutPosicional) {
		this.layoutPosicionalService.delete(this.layoutPosicionalConverter.convertListJsonFrom(layoutPosicional));
	}

	@ApiOperation(value = "Serviço de remoção de todos os Layouts")
	@RequestMapping(method = RequestMethod.DELETE, path = "/delete/all")
	public void deleteAll() {
		this.layoutPosicionalService.deleteAll();
	}

}
