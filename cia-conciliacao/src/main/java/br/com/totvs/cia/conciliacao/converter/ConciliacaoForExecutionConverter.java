package br.com.totvs.cia.conciliacao.converter;

import java.util.Date;

import org.springframework.batch.item.file.transform.ConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.carga.json.StatusCargaJsonEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.service.CargaService;
import br.com.totvs.cia.conciliacao.json.ConciliacaoExecucaoJson;
import br.com.totvs.cia.conciliacao.model.ConciliacaoForExecution;
import br.com.totvs.cia.importacao.json.StatusImportacaoJsonEnum;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.infra.converter.JsonConverter;

@Component
public class ConciliacaoForExecutionConverter extends JsonConverter<ConciliacaoForExecution, ConciliacaoExecucaoJson> {
	
	private Date data;
	
	@Autowired
	private CargaService cargaService;
	
	@Autowired
	private ImportacaoService importacaoService;
	
	@Override
	public ConciliacaoExecucaoJson convertFrom(final ConciliacaoForExecution model) {
		StatusCargaJsonEnum statusCarga = StatusCargaJsonEnum.NAO_EXECUTADO;
		StatusImportacaoJsonEnum statusImportacao = StatusImportacaoJsonEnum.NAO_EXECUTADO;
		if (model.getCarga() != null) {
			Carga carga = this.cargaService.findBy(model.getCarga());
			statusCarga = StatusCargaJsonEnum.fromCodigo(carga.getStatus().getCodigo());
		}
		if (model.getImportacao() != null) {
			Importacao importacao = this.importacaoService.findBy(model.getImportacao());
			statusImportacao = StatusImportacaoJsonEnum.fromCodigo(importacao.getStatus().getCodigo());
		}
		return new ConciliacaoExecucaoJson(model, this.data, statusCarga, statusImportacao);
	}

	@Override
	public ConciliacaoForExecution convertFrom(final ConciliacaoExecucaoJson json) {
		throw new ConversionException("Conversão de ConciliacaoExecucaoJson para ConciliacaoForExecution não prevista.");
	}
	
	public ConciliacaoForExecutionConverter of (final Date data) {
		this.data = data;
		return this;
	}
}