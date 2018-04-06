package br.com.totvs.cia.parametrizacao.layout.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;

import br.com.totvs.cia.parametrizacao.layout.converter.LayoutConverter;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutDelimitador;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutPosicional;
import br.com.totvs.cia.parametrizacao.layout.model.LayoutXml;
import br.com.totvs.cia.parametrizacao.layout.model.StatusLayoutEnum;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutDelimitadorService;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutPosicionalService;
import br.com.totvs.cia.parametrizacao.layout.service.LayoutXmlService;
import io.swagger.annotations.Api;

@RestController
@RequestMapping(value = "/api/v1/parametrizacao/layout/listBy")
@Api(value = "Layout")
public class LayoutResource {

	@Autowired
	private LayoutPosicionalService layoutPosicionalService;
	
	@Autowired
	private LayoutDelimitadorService layoutDelimitadorService;

	@Autowired
	private LayoutXmlService layoutXmlService;

	@Autowired
	private LayoutConverter layoutConverter;

	@RequestMapping(value = "{ALL}", method = RequestMethod.GET)
	public ResponseEntity<List<LayoutJson>> listAll(@PathVariable("ALL") final String allLayouts) {

		List<LayoutJson> layouts = Lists.newArrayList();

		for (LayoutPosicional layoutPosicional : this.layoutPosicionalService.findAll(StatusLayoutEnum.ATIVO)) {
			layouts.add(this.layoutConverter.convertFrom(layoutPosicional));
		}

		for (LayoutXml layoutXml : this.layoutXmlService.findAll(StatusLayoutEnum.ATIVO)) {
			layouts.add(this.layoutConverter.convertFrom(layoutXml));
		}

		return new ResponseEntity<List<LayoutJson>>(layouts, HttpStatus.OK);

	}

	@RequestMapping(value = "{POSICIONAL}", method = RequestMethod.GET)
	public ResponseEntity<List<LayoutJson>> listLayouPosicional(@PathVariable("POSICIONAL") final String tipoLayout) {

		List<LayoutJson> layouts = Lists.newArrayList();

		List<LayoutPosicional> layoutsModel = this.layoutPosicionalService.findAll(StatusLayoutEnum.ATIVO);

		for (LayoutPosicional layoutPosicional : layoutsModel) {
			layouts.add(this.layoutConverter.convertFrom(layoutPosicional));
		}

		return new ResponseEntity<List<LayoutJson>>(layouts, HttpStatus.OK);
	}
	
	@RequestMapping(value = "{DELIMITADOR}", method = RequestMethod.GET)
	public ResponseEntity<List<LayoutJson>> listLayouDelimitador(@PathVariable("DELIMITADOR") final String tipoLayout) {

		List<LayoutJson> layouts = Lists.newArrayList();

		List<LayoutDelimitador> layoutsModel = this.layoutDelimitadorService.findAll(StatusLayoutEnum.ATIVO);

		for (LayoutDelimitador layoutDelimitador : layoutsModel) {
			layouts.add(this.layoutConverter.convertFrom(layoutDelimitador));
		}

		return new ResponseEntity<List<LayoutJson>>(layouts, HttpStatus.OK);
	}

	@RequestMapping(value = "{XML}", method = RequestMethod.GET)
	public ResponseEntity<List<LayoutJson>> listLayouXml(@PathVariable("XML") final String tipoLayout) {

		List<LayoutJson> layouts = Lists.newArrayList();

		List<LayoutXml> layoutsModel = this.layoutXmlService.findAll(StatusLayoutEnum.ATIVO);

		for (LayoutXml layoutXml : layoutsModel) {
			layouts.add(this.layoutConverter.convertFrom(layoutXml));
		}

		return new ResponseEntity<List<LayoutJson>>(layouts, HttpStatus.OK);
	}

}
