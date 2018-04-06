package br.com.totvs.cia.cadastro.configuracaoservico.resource;

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
import br.com.totvs.cia.cadastro.configuracaoservico.converter.ConfiguracaoServicoConverter;
import br.com.totvs.cia.cadastro.configuracaoservico.json.ConfiguracaoServicoJson;
import br.com.totvs.cia.cadastro.configuracaoservico.json.ServicoJsonEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.json.TipoServicoJsonEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.TipoServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.service.ConfiguracaoServicoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/api/v1/cadastro/configuracaoservico")
@Api(value = "Configuração de Serviço")
public class ConfiguracaoServicoResource {

	@Autowired
	private ConfiguracaoServicoService service;
	
	@Autowired
	private ConfiguracaoServicoConverter converter;
	
	@ApiOperation(value = "Serviço de consulta de Configurações de Serviço por filtros")
	@RequestMapping(method = RequestMethod.GET, 
					path = "/search", 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ConfiguracaoServicoJson>> search(
			@ApiParam(required = false) @RequestParam(required = false) final String codigo,
			@ApiParam(required = false) @RequestParam(required = false) final String descricao,
			@ApiParam(required = false) @RequestParam(required = false) final SistemaJsonEnum sistema,
			@ApiParam(required = false) @RequestParam(required = false) final ServicoJsonEnum servico,
			@ApiParam(required = false) @RequestParam(required = false) final TipoServicoJsonEnum tipoServico) {
		
		SistemaEnum sistemaEnum = sistema != null ? SistemaEnum.fromCodigo(sistema.getCodigo()): null;
		ServicoEnum servicoEnum = servico != null ? ServicoEnum.fromCodigo(servico.getCodigo()): null;
		TipoServicoEnum tipoServicoEnum = tipoServico != null ? TipoServicoEnum.fromCodigo(tipoServico.getCodigo()): null;

		List<ConfiguracaoServico> configuracoes = this.service.search(codigo, descricao, sistemaEnum, servicoEnum, tipoServicoEnum);

		List<ConfiguracaoServicoJson> jsons = this.converter.convertListModelFrom(configuracoes);
		
		return new ResponseEntity<List<ConfiguracaoServicoJson>>(jsons, HttpStatus.OK);
	}
	
	@RequestMapping(path = "/by", method = RequestMethod.GET)
	public ResponseEntity<List<ConfiguracaoServicoJson>> getBy(final SistemaJsonEnum sistema) {
		
		List<ConfiguracaoServico> servicos = this.service.findBy(SistemaEnum.fromCodigo(sistema.getCodigo()));
		
		List<ConfiguracaoServicoJson> servicosJson = this.converter.convertListModelFrom(servicos);
		
		return new ResponseEntity<List<ConfiguracaoServicoJson>>(servicosJson, HttpStatus.OK);
		
	}
	
	@ApiOperation(value = "Serviço de inclusão de uma Configuração de Serviço")
	@RequestMapping(method = RequestMethod.POST, 
					path = "/incluir", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ConfiguracaoServicoJson> incluir(@RequestBody final ConfiguracaoServicoJson json) {
		
		ConfiguracaoServico model = this.converter.convertFrom(json);

		ConfiguracaoServico novoConfiguracaoServico = this.service.incluir(model);
		
		ConfiguracaoServicoJson configuracaoServicoJson = this.converter.convertFrom(novoConfiguracaoServico);

		return new ResponseEntity<ConfiguracaoServicoJson>(configuracaoServicoJson, HttpStatus.OK);

	}

	@ApiOperation(value = "Serviço de Alteração de uma Configuração de Serviço")
	@RequestMapping(method = RequestMethod.PUT, 
					path = "/alterar", 
					consumes = { MediaType.APPLICATION_JSON_VALUE }, 
					produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<ConfiguracaoServicoJson> alterar(@RequestBody final ConfiguracaoServicoJson json) {
		
		ConfiguracaoServico model = this.converter.convertFrom(json);

		ConfiguracaoServico novaConfiguracaoServico = this.service.alterar(model);
		
		ConfiguracaoServicoJson configuracaoServicoJson = this.converter.convertFrom(novaConfiguracaoServico);

		return new ResponseEntity<ConfiguracaoServicoJson>(configuracaoServicoJson, HttpStatus.OK);
	}

	@ApiOperation(value = "Serviço de remoção de Configurações de Serviços")
	@RequestMapping(method = RequestMethod.DELETE, 
					path = "/delete", 
					consumes = { MediaType.APPLICATION_JSON_VALUE })
	public void delete(@RequestBody final List<ConfiguracaoServicoJson> json) {
		
		List<ConfiguracaoServico> models = this.converter.convertListJsonFrom(json);
		
		this.service.delete(models);
	}
}