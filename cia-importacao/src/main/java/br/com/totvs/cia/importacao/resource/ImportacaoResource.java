package br.com.totvs.cia.importacao.resource;

import static br.com.totvs.cia.importacao.repository.ImportacaoSpecification.listBy;
import static org.springframework.data.jpa.domain.Specifications.where;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.importacao.converter.ArquivoConverter;
import br.com.totvs.cia.importacao.converter.ImportacaoConverter;
import br.com.totvs.cia.importacao.json.ArquivoJson;
import br.com.totvs.cia.importacao.json.ImportacaoJson;
import br.com.totvs.cia.importacao.model.Arquivo;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(path = "/api/v1/importacao")
@Api(value = "Importacao de Arquivo")
public class ImportacaoResource {
	
	@Autowired
	private ArquivoConverter arquivoConverter;
	
	@Autowired
	private ImportacaoConverter	importacaoConverter;
	
	@Autowired
	private ImportacaoService importacaoService;
	
	@ApiOperation(value = "Serviço de consulta das Importações")
	@RequestMapping(method = RequestMethod.GET, path = "/list", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<ImportacaoJson>> list(
			@RequestParam(required = false) final String data,
			@RequestParam(required = false) final SistemaJsonEnum sistema,
			@RequestParam(required = false) final String corretora,
			@RequestParam(required = false) final TipoLayoutEnumJson tipoLayout,
			@RequestParam(required = false) final String layout) {

		try {
			
			final Date dataImportacao = !"".equals(data) ? DateUtil.parse(data, DateUtil.yyyy_MM_dd) : null;
			
			final SistemaEnum sistemaEnum = sistema != null ? SistemaEnum.fromCodigo(sistema.getCodigo()) : null;

			final TipoLayoutEnum tipoLayoutEnum = tipoLayout != null ? TipoLayoutEnum.fromCodigo(tipoLayout.getCodigo()) : null;
			
			final List<Importacao> importacoesPersistidas = this.importacaoService.findAll(where(listBy(dataImportacao, sistemaEnum, corretora, tipoLayoutEnum, layout)), new Sort("dataImportacao"));
			
			final List<ImportacaoJson> importacoes = this.importacaoConverter.convertListModelFrom(importacoesPersistidas);
			
			return new ResponseEntity<List<ImportacaoJson>>(importacoes, HttpStatus.OK);
		
		} catch (final ParseException e) {
			return new ResponseEntity<List<ImportacaoJson>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	
	@RequestMapping(path = "/incluirArquivo", method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ArquivoJson> incluirArquivo(@RequestParam(value = "file") final MultipartFile file, @RequestParam(value = "id") final String idFile) {
		
		final Arquivo arquivoPersistido = this.importacaoService.saveFile(file, idFile);
				
		final ArquivoJson arquivoJson = this.arquivoConverter.convertFrom(arquivoPersistido);
		
		return new ResponseEntity<ArquivoJson>(arquivoJson, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Serviço de inclusão das Importações")
	@RequestMapping(path = "/incluir", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<ImportacaoJson> importar(@RequestBody(required = true)final ImportacaoJson importacao) {
		
		final Importacao importacaoConvertida = this.importacaoConverter.convertFrom(importacao);
		
		final Importacao importacaoPersistida = this.importacaoService.executar(importacaoConvertida);
		
		final ImportacaoJson importacaoJson = this.importacaoConverter.convertFrom(importacaoPersistida);
		
		return new ResponseEntity<ImportacaoJson>(importacaoJson, HttpStatus.OK);
	}
	
}
