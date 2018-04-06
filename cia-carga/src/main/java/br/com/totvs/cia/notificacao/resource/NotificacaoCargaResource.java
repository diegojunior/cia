package br.com.totvs.cia.notificacao.resource;

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

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.service.CargaService;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.notificacao.converter.NotificacaoCargaConverter;
import br.com.totvs.cia.notificacao.json.CargaComNotificacoesJson;
import br.com.totvs.cia.notificacao.model.NotificacaoCarga;
import br.com.totvs.cia.notificacao.service.NotificacaoCargaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api/v1/notificacao/carga")
@Api(value = "Notificações de Carga")
public class NotificacaoCargaResource {
	
	@Autowired
	private NotificacaoCargaService notificacaoCargaService;
	
	@Autowired
	private CargaService cargaService;
	
	@Autowired
	private NotificacaoCargaConverter notificacaoCargaConverter;
	
	@ApiOperation(value = "Serviço de consulta das Notificações de carga pela data, sistema e serviço que efetuou a carga.")
	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<CargaComNotificacoesJson>> getBy(@RequestParam(required = false) final String data, 
			@RequestParam(required = false) final SistemaJsonEnum sistema, 
			@RequestParam(required = false) final String servico) {
		try {
			final Date dataCarga = data != null ? DateUtil.parse(data,DateUtil.yyyy_MM_dd): null;
			final SistemaEnum sistemaEnum = sistema != null ? SistemaEnum.fromCodigo(sistema.getCodigo()) : null;
			final List<Carga> cargas = this.cargaService.listBy(dataCarga, sistemaEnum, servico);
			final List<NotificacaoCarga> models = this.notificacaoCargaService.findBy(cargas);
			
			List<CargaComNotificacoesJson> jsons = this.notificacaoCargaConverter.convertListModelFrom(models);
			return new ResponseEntity<List<CargaComNotificacoesJson>>(jsons, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<List<CargaComNotificacoesJson>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}