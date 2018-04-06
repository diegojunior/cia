package br.com.totvs.cia.conciliacao.resource;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.batch.item.file.transform.ConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.totvs.cia.conciliacao.converter.ConciliacaoCompletaConverter;
import br.com.totvs.cia.conciliacao.converter.ConciliacaoForExecutionConverter;
import br.com.totvs.cia.conciliacao.converter.ConciliacaoResumidaConverter;
import br.com.totvs.cia.conciliacao.json.ConciliacaoCompletaJson;
import br.com.totvs.cia.conciliacao.json.ConciliacaoExecucaoJson;
import br.com.totvs.cia.conciliacao.json.ConciliacaoResumidaJson;
import br.com.totvs.cia.conciliacao.json.StatusConciliacaoJsonEnum;
import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.model.ConciliacaoForExecution;
import br.com.totvs.cia.conciliacao.model.StatusConciliacaoEnum;
import br.com.totvs.cia.conciliacao.service.ConciliacaoASyncService;
import br.com.totvs.cia.conciliacao.service.ConciliacaoService;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.service.PerfilConciliacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/conciliacao")
@Api(value = "Conciliação")
public class ConciliacaoResource {
	
	@Autowired
	private ConciliacaoService conciliacaoService;
	
	@Autowired
	private ConciliacaoASyncService conciliacaoASyncService;

	@Autowired
	private ConciliacaoForExecutionConverter conciliacaoForExecutionConverter;
	
	@Autowired
	private ConciliacaoCompletaConverter conciliacaoCompletaConverter;

	@Autowired
	private ConciliacaoResumidaConverter conciliacaoResumidaConverter;
	
	@Autowired
	private PerfilConciliacaoService perfilConciliacaoService;

	@ApiOperation(value = "Serviço de consulta de Conciliações por filtros")
	@RequestMapping(method = RequestMethod.GET, 
				   path = "/search", 
				   produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ConciliacaoResumidaJson>> search(
			@RequestParam(required = false) final String data,
			@RequestParam(required = false) final String perfil,
			@RequestParam(required = false) final StatusConciliacaoJsonEnum statusJson) {
		try {
			final Date dataConciliacao = data != null && !"".equals(data) ? DateUtil.parse(data, DateUtil.yyyy_MM_dd) : null;
			final StatusConciliacaoEnum status = statusJson != null ? StatusConciliacaoEnum.fromCodigo(statusJson.getCodigo()): null;
			
			final List<Conciliacao> models = this.conciliacaoService.search(dataConciliacao, perfil, status);
			final List<ConciliacaoResumidaJson> jsons = this.conciliacaoResumidaConverter.convertListModelFrom(models);

			return new ResponseEntity<List<ConciliacaoResumidaJson>>(jsons, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<List<ConciliacaoResumidaJson>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Serviço de consulta de possíveis Conciliações para Execução")
	@RequestMapping(method = RequestMethod.GET, 
				   path = "/searchforexecution", 
				   produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ConciliacaoExecucaoJson>> searchForExecution(
			@RequestParam(required = false) final String data,
			@RequestParam(required = false) final String perfil) {
		try {
			final Date dataConciliacao = DateUtil.parse(data, DateUtil.yyyy_MM_dd);
			final List<ConciliacaoForExecution> models = this.conciliacaoService.buscaConciliacoesParaExecucao(dataConciliacao, perfil);
			final List<ConciliacaoExecucaoJson> jsons = this.conciliacaoForExecutionConverter.of(dataConciliacao).convertListModelFrom(models);

			return new ResponseEntity<List<ConciliacaoExecucaoJson>>(jsons, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<List<ConciliacaoExecucaoJson>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Serviço de consulta de Conciliação Completa por data e id do perfil")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/full", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ConciliacaoCompletaJson> findFull(
			@RequestParam final String data, 
			@RequestParam final String perfil) {
		try {
			final Date dataConciliacao = DateUtil.parse(data, DateUtil.yyyy_MM_dd);
			final Conciliacao conciliacao = this.conciliacaoService.getFullBy(dataConciliacao, perfil);
			final ConciliacaoCompletaJson conciliacaoJson = this.conciliacaoCompletaConverter.convertFrom(conciliacao);
			
			return new ResponseEntity<ConciliacaoCompletaJson>(conciliacaoJson, HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<ConciliacaoCompletaJson>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, 
					path = "/executar", 
					consumes = {MediaType.APPLICATION_JSON_VALUE}, 
					produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ConciliacaoExecucaoJson> executar(@RequestBody final List<ConciliacaoExecucaoJson> conciliacoesJson) {
		for(final ConciliacaoExecucaoJson json : conciliacoesJson) {
			try {
				Date data = DateUtil.parse(json.getData(), DateUtil.yyyy_MM_dd);
				final PerfilConciliacao perfil = this.perfilConciliacaoService.findByCodigo(json.getPerfil());
				this.conciliacaoService.removeConciliacoesNaoGravadas(data, perfil);
				this.conciliacaoASyncService.executar(json);
			} catch (ParseException e) {
				throw new ConversionException("Erro ao realizar o Parse da Data: "+ json.getData());
			}
		}
		return new ResponseEntity<ConciliacaoExecucaoJson>(HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço para Gravar uma Conciliação")
	@RequestMapping(method = RequestMethod.POST, 
					path = "/gravar", 
					consumes = {MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ConciliacaoCompletaJson> gravar(@RequestBody final ConciliacaoCompletaJson json) {
		final Conciliacao model = this.conciliacaoCompletaConverter.convertFrom(json);
		final Conciliacao conciliacaoGravada = this.conciliacaoService.gravar(model);
		final ConciliacaoCompletaJson conciliacaoJsonGravada = this.conciliacaoCompletaConverter.convertFrom(conciliacaoGravada);

		return new ResponseEntity<ConciliacaoCompletaJson>(conciliacaoJsonGravada, HttpStatus.OK);
	}
}