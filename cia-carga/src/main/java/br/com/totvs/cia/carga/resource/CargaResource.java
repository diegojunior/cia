package br.com.totvs.cia.carga.resource;

import java.util.Date;
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

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.carga.converter.CargaDetalheConverter;
import br.com.totvs.cia.carga.converter.CargaExecucaoConverter;
import br.com.totvs.cia.carga.json.CargaCompletaJson;
import br.com.totvs.cia.carga.json.CargaExecucaoJson;
import br.com.totvs.cia.carga.json.StatusCargaJsonEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.StatusCargaEnum;
import br.com.totvs.cia.carga.service.CargaService;
import br.com.totvs.cia.infra.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/carga")
@Api(value = "Carga")
public class CargaResource {

	@Autowired
	private CargaService cargaService;
	
	@Autowired
	private CargaDetalheConverter cargaDetalheConverter;
	
	@Autowired
	private CargaExecucaoConverter cargaExecucaoConverter;
	
	@ApiOperation(value = "Serviço de consulta de Cargas por Data ou Sistema")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/search", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<CargaCompletaJson>> search(
			@RequestParam(required = false) final String data, 
			@RequestParam(required = false) final SistemaJsonEnum sistema,
			@RequestParam(required = false) final StatusCargaJsonEnum status) {
		try {
			Date dataPosicao = !"".equals(data) ? DateUtil.parse(data, DateUtil.yyyy_MM_dd) : null;
			SistemaEnum sistemaModel = sistema != null ? SistemaEnum.fromCodigo(sistema.getCodigo()) : null;
			StatusCargaEnum statusModel = status != null ? StatusCargaEnum.fromCodigo(status.getCodigo()) : null;
	
			List<Carga> cargas = this.cargaService.search(dataPosicao, sistemaModel, statusModel);
			List<CargaCompletaJson> cargasJson = this.cargaDetalheConverter.convertListModelFrom(cargas);
	
			return new ResponseEntity<List<CargaCompletaJson>>(cargasJson, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<CargaCompletaJson>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@ApiOperation(value = "Serviço de Execução de Carga")
	@RequestMapping(method = RequestMethod.POST, 
					path = "/executar",
					consumes = { MediaType.APPLICATION_JSON_VALUE },
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<CargaExecucaoJson> executar(@RequestBody final CargaExecucaoJson cargaJson) throws Exception {
		Carga carga = this.cargaExecucaoConverter.convertFrom(cargaJson);
		this.cargaService.executa(carga, cargaJson.getTipoExecucao());
		return new ResponseEntity<CargaExecucaoJson>(HttpStatus.OK);
	}
}