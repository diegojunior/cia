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

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.repository.ImportacaoSpecification;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.notificacao.converter.NotificacaoImportacaoConverter;
import br.com.totvs.cia.notificacao.json.ImportacaoComNotificacoesJson;
import br.com.totvs.cia.notificacao.model.NotificacaoImportacao;
import br.com.totvs.cia.notificacao.service.NotificacaoImportacaoService;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api/v1/notificacao/importacao")
@Api(value = "Notificações")
public class NotificacaoImportacaoResource {
	
	@Autowired
	private NotificacaoImportacaoService notificacaoImportacaoService;
	
	@Autowired
	private ImportacaoService importacaoService;
	
	@Autowired
	private NotificacaoImportacaoConverter notificacaoImportacaoConverter;
	
	@ApiOperation(value = "Serviço de consulta das Notificações de importacao pela data e código da importação")
	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ImportacaoComNotificacoesJson>> getBy(@RequestParam(required = false)final String data, 
			@RequestParam(required = false)final SistemaJsonEnum sistema,
			@RequestParam(required = false) final TipoLayoutEnumJson tipoLayout,
			@RequestParam(required = false) final String codigoLayout) {

		final List<ImportacaoComNotificacoesJson> notificacoes = Lists.newArrayList();
		
		try {
			
			final Date dataImportacao = data != null ? DateUtil.parse(data,DateUtil.yyyy_MM_dd): null;
			final SistemaEnum sistemaEnum = sistema != null ? SistemaEnum.fromCodigo(sistema.getCodigo()) : null;
			final TipoLayoutEnum tipoLayoutEnum = tipoLayout != null ? TipoLayoutEnum.fromCodigo(tipoLayout.getCodigo()) : null;

			final List<Importacao> importacoes = this.importacaoService.findAll(ImportacaoSpecification.listBy(dataImportacao, sistemaEnum, null, tipoLayoutEnum, codigoLayout));
			final List<NotificacaoImportacao> notificacoesPersistidas = this.notificacaoImportacaoService.findAll(importacoes);
			
			notificacoes.addAll(this.notificacaoImportacaoConverter.convertListModelFrom(notificacoesPersistidas));
	
		} catch (final Exception e) {
			return new ResponseEntity<List<ImportacaoComNotificacoesJson>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return new ResponseEntity<List<ImportacaoComNotificacoesJson>>(notificacoes, HttpStatus.OK);

	}
	
}
