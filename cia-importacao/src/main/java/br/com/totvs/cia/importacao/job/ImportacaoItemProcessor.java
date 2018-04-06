package br.com.totvs.cia.importacao.job;

import java.util.List;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import br.com.totvs.cia.importacao.converter.UnidadeImportacaoConverter;
import br.com.totvs.cia.importacao.exception.ValidacaoInternaException;
import br.com.totvs.cia.importacao.json.UnidadeImportacaoProcessamentoJson;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.model.UnidadeLayoutImportacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.dominio.service.DominioService;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoService;
import br.com.totvs.cia.parametrizacao.validacao.model.AbstractValidacaoArquivo;
import br.com.totvs.cia.parametrizacao.validacao.model.LocalValidacaoArquivoEnum;
import br.com.totvs.cia.parametrizacao.validacao.model.TipoValidacaoEnum;
import br.com.totvs.cia.parametrizacao.validacao.service.ValidacaoArquivoService;

@Component
public class ImportacaoItemProcessor implements ItemProcessor<UnidadeImportacaoProcessamentoJson, UnidadeLayoutImportacao> {

	@Autowired
	private UnidadeImportacaoConverter unidadeImportacaoConverter;

	@Autowired
	private ImportacaoService importacaoService;

	@Autowired
	private DominioService dominioService;
	
	@Autowired
	private ValidacaoArquivoService validacaoService;
	
	@Autowired
	private TransformacaoExecutor transformacaoExecutor;
	
	@Autowired
	private TransformacaoService transformacaoService;

	private List<Dominio> dominios = Lists.newArrayList();
	
	private List<AbstractValidacaoArquivo> validacoesInternas;

	private List<Transformacao> transformacoes;
	
	private Importacao importacao;


	public void carregaDominios() {
		this.dominios = this.dominioService.findAll();
	}
	
	public void carregaValidacoesTransformacoes(final String importacaoId) {
		this.importacao = this.importacaoService.loadImportacaoAndUnidade(importacaoId);
		this.validacoesInternas = this.validacaoService.search(this.importacao.getLayout().getTipoLayout(), 
				importacao.getLayout().getCodigo(), 
				TipoValidacaoEnum.ARQUIVO, 
				null, 
				LocalValidacaoArquivoEnum.INTERNO);
		this.transformacoes = this.listTransformacoes(importacao);
		this.transformacaoExecutor.config(this.importacao.getSistema(), transformacoes);
	}

	@Override
	public UnidadeLayoutImportacao process(final UnidadeImportacaoProcessamentoJson item) throws Exception {
		if (item.getCampos().isEmpty()) {
			return null;
		}
		final UnidadeLayoutImportacao unidade = this.unidadeImportacaoConverter.convertFrom(item, this.dominios);
		this.importacao.adicionarUnidadeImportacao(unidade);
		this.transformacaoExecutor.transformar(unidade.getCampos(), transformacoes);
		if (this.importacao.arquivoDesaprovadoPelaValidacaoInterna(unidade, this.validacoesInternas)) {
			throw new ValidacaoInternaException("Corretora e(ou) Data informada esta inválida. Verifique Parametrização de Validação.");
		}
		return unidade;

	}
	
	private List<Transformacao> listTransformacoes(final Importacao importacao) {
		List<Transformacao> transformacoes = this.transformacaoService.getBy(importacao.getLayout().getTipoLayout(), 
				importacao.getLayout().getCodigo());
		return transformacoes;
	}

}