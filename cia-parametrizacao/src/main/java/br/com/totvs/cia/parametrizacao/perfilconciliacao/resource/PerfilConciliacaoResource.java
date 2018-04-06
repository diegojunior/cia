package br.com.totvs.cia.parametrizacao.perfilconciliacao.resource;

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

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.converter.PerfilConciliacaoConverter;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.PerfilConciliacaoJson;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.StatusPerfilJsonEnum;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.StatusPerfilEnum;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.service.PerfilConciliacaoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v1/parametrizacao/perfilconciliacao")
@Api(value = "Perfil Conciliação")
public class PerfilConciliacaoResource {

	@Autowired
	private PerfilConciliacaoService service;
	
	@Autowired
	private PerfilConciliacaoConverter converter;
	
	@ApiOperation(value = "Serviço de consulta dos Perfis de Conciliação por nome e tipo de Sistema e Layout")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/search", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<PerfilConciliacaoJson>> search(
			@RequestParam(required = false) final String codigo,
			@RequestParam(required = false) final String descricao,
			@RequestParam(required = false) final SistemaJsonEnum sistema,			
			@RequestParam(required = false) final TipoLayoutEnumJson tipoLayout,
			@RequestParam(required = false) final String layout,
			@RequestParam(required = false) final StatusPerfilJsonEnum status) {
		try {
			SistemaEnum sistemaModel = sistema != null ? SistemaEnum.fromCodigo(sistema.getCodigo()) : null;
			TipoLayoutEnum tipoLayoutModel = tipoLayout != null ? TipoLayoutEnum.fromCodigo(tipoLayout.getCodigo()) : null;
			StatusPerfilEnum statusModel = status != null ? StatusPerfilEnum.fromCodigo(status.getCodigo()) : null;
			
			List<PerfilConciliacao> models = this.service.search(codigo, descricao, sistemaModel, tipoLayoutModel, layout, statusModel);
			List<PerfilConciliacaoJson> json = this.converter.convertListModelFrom(models);
			
			return new ResponseEntity<List<PerfilConciliacaoJson>>(json, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<PerfilConciliacaoJson>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Serviço de consulta de Perfis Ativos")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/ativos", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<PerfilConciliacaoJson>> getAtivos() {
		try {
			List<PerfilConciliacao> models = this.service.getAtivos();
			List<PerfilConciliacaoJson> json = this.converter.convertListModelFrom(models);
			
			return new ResponseEntity<List<PerfilConciliacaoJson>>(json, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<PerfilConciliacaoJson>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@ApiOperation(value = "Serviço de inclusão dos Perfis de Conciliação")
	@RequestMapping(method = RequestMethod.POST, 
					path = "/incluir", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PerfilConciliacaoJson> incluir(@RequestBody final PerfilConciliacaoJson perfilJson) {
		
		PerfilConciliacao perfilConciliacao = this.converter.convertFrom(perfilJson);
		
		this.service.save(perfilConciliacao);

		return new ResponseEntity<PerfilConciliacaoJson>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço para Ativar um Perfil de Conciliação")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/ativar", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PerfilConciliacaoJson> ativar(@RequestBody final String id) {
		
		PerfilConciliacao perfilAtivado = this.service.ativar(id);

		return new ResponseEntity<PerfilConciliacaoJson>(this.converter.convertFrom(perfilAtivado), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço para Inativar um Perfil de Conciliação")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/inativar", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<PerfilConciliacaoJson> inativar(@RequestBody final String id) {
		
		PerfilConciliacao perfilInativo = this.service.inativar(id);

		return new ResponseEntity<PerfilConciliacaoJson>(this.converter.convertFrom(perfilInativo), HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de exclusão dos Perfis de Conciliação")
	@RequestMapping(method = RequestMethod.DELETE, path = "/delete", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@RequestBody final List<PerfilConciliacaoJson> perfis) {

		List<PerfilConciliacao> perfisExcluir = Lists.newArrayList();

		for (PerfilConciliacaoJson perfilConciliacaoJson : perfis) {
			perfisExcluir.add(this.converter.convertFrom(perfilConciliacaoJson));
		}

		this.service.delete(perfisExcluir);
	}
}