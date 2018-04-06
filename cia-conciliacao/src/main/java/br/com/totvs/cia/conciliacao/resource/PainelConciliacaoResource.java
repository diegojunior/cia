package br.com.totvs.cia.conciliacao.resource;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.totvs.cia.conciliacao.converter.PainelConciliacaoConverter;
import br.com.totvs.cia.conciliacao.json.PainelConciliacaoJson;
import br.com.totvs.cia.conciliacao.model.ConciliacaoForPainel;
import br.com.totvs.cia.conciliacao.service.PainelConciliacaoService;
import br.com.totvs.cia.infra.util.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/painel")
@Api(value = "Painel de Conciliação")
public class PainelConciliacaoResource {
	
	@Autowired
	private PainelConciliacaoService service;

	@Autowired
	private PainelConciliacaoConverter converter;
	
	@ApiOperation(value = "Serviço de consulta de Conciliações por filtros")
	@RequestMapping(method = RequestMethod.GET, 
				   path = "/conciliacao", 
				   produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<PainelConciliacaoJson>> conciliacao(
			@RequestParam(required = false) final String data) {
		try {
			final Date dataConciliacao = data != null && !"".equals(data) ? DateUtil.parse(data, DateUtil.yyyy_MM_dd) : null;
			final List<ConciliacaoForPainel> models = this.service.listBy(dataConciliacao);
			final List<PainelConciliacaoJson> jsons = this.converter.of(dataConciliacao).convertListModelFrom(models);

			return new ResponseEntity<List<PainelConciliacaoJson>>(jsons, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<List<PainelConciliacaoJson>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}