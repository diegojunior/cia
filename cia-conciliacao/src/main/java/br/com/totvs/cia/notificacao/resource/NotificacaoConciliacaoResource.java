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

import br.com.totvs.cia.conciliacao.json.StatusConciliacaoJsonEnum;
import br.com.totvs.cia.conciliacao.model.StatusConciliacaoEnum;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.notificacao.converter.NotificacaoConciliacaoConverter;
import br.com.totvs.cia.notificacao.json.ConciliacaoComNotificacoesJson;
import br.com.totvs.cia.notificacao.model.NotificacaoConciliacao;
import br.com.totvs.cia.notificacao.service.NotificacaoConciliacaoService;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.service.PerfilConciliacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api/v1/notificacao/conciliacao")
@Api(value = "Notificações")
public class NotificacaoConciliacaoResource {
	
	@Autowired
	private PerfilConciliacaoService perfilService; 
	
	@Autowired
	private NotificacaoConciliacaoService notificacaoConciliacaoService;
	
	@Autowired
	private NotificacaoConciliacaoConverter notificacaoImportacaoConverter;
	
	@ApiOperation(value = "Serviço de consulta das Notificações da concilição pela data, perfil e status")
	@RequestMapping(method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ConciliacaoComNotificacoesJson>> getBy(@RequestParam(required = false) final String dataConciliacao,
			@RequestParam(required = false) final String codigoPerfil,
			@RequestParam(required = false) final StatusConciliacaoJsonEnum statusConciliacao) {

		final List<ConciliacaoComNotificacoesJson> notificacoes = Lists.newArrayList();
		
		try {
			
			final Date data = dataConciliacao != null 
					?  DateUtil.parse(dataConciliacao, DateUtil.yyyy_MM_dd) 
							: null;
			
			List<PerfilConciliacao> perfis = codigoPerfil != null 
					? this.perfilService.findAllBy(codigoPerfil) :
						null;		
					
			StatusConciliacaoEnum status = statusConciliacao != null 
					? StatusConciliacaoEnum.fromCodigo(statusConciliacao.getCodigo())
					: null;
					
			final List<NotificacaoConciliacao> notificacoesPersistidas = this.notificacaoConciliacaoService.findAll(data, perfis, status);
			
			notificacoes.addAll(this.notificacaoImportacaoConverter.convertListModelFrom(notificacoesPersistidas));
					
		} catch (final Exception e) {
			return new ResponseEntity<List<ConciliacaoComNotificacoesJson>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		

		return new ResponseEntity<List<ConciliacaoComNotificacoesJson>>(notificacoes, HttpStatus.OK);

	}
	
}
