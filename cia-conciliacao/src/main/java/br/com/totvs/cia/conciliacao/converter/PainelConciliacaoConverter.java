package br.com.totvs.cia.conciliacao.converter;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.carga.json.StatusCargaJsonEnum;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.service.CargaService;
import br.com.totvs.cia.conciliacao.json.PainelConciliacaoJson;
import br.com.totvs.cia.conciliacao.json.StatusConciliacaoJsonEnum;
import br.com.totvs.cia.conciliacao.model.Conciliacao;
import br.com.totvs.cia.conciliacao.model.ConciliacaoForPainel;
import br.com.totvs.cia.conciliacao.service.PainelConciliacaoService;
import br.com.totvs.cia.importacao.json.StatusImportacaoJsonEnum;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.service.ImportacaoService;
import br.com.totvs.cia.infra.converter.JsonConverter;
import br.com.totvs.cia.infra.exception.ConverterException;

@Component
public class PainelConciliacaoConverter extends JsonConverter<ConciliacaoForPainel, PainelConciliacaoJson> {
	
	private static final Logger log = Logger.getLogger(PainelConciliacaoConverter.class);
	
	private Date data;
	
	@Autowired
	private CargaService cargaService;
	
	@Autowired
	private ImportacaoService importacaoService;
	
	@Autowired
	private PainelConciliacaoService painelConciliacaoService;
	
	@Override
	public PainelConciliacaoJson convertFrom(final ConciliacaoForPainel model) {
		try {
			StatusCargaJsonEnum statusCarga = StatusCargaJsonEnum.NAO_EXECUTADO;
			StatusImportacaoJsonEnum statusImportacao = StatusImportacaoJsonEnum.NAO_EXECUTADO;
			StatusConciliacaoJsonEnum statusConciliacao = StatusConciliacaoJsonEnum.NAO_EXECUTADO;
			if (model.getCarga() != null) {
				Carga carga = this.cargaService.findBy(model.getCarga());
				statusCarga = StatusCargaJsonEnum.fromCodigo(carga.getStatus().getCodigo());
			}
			if (model.getImportacao() != null) {
				Importacao importacao = this.importacaoService.findBy(model.getImportacao());
				statusImportacao = StatusImportacaoJsonEnum.fromCodigo(importacao.getStatus().getCodigo());
			}
			if (model.getConciliacao() != null) {
				Conciliacao conciliacao = this.painelConciliacaoService.findOneBy(model.getConciliacao());
				statusConciliacao = StatusConciliacaoJsonEnum.fromCodigo(conciliacao.getStatus().getCodigo());
			}
			return new PainelConciliacaoJson(model, this.data, statusCarga, statusImportacao, statusConciliacao);
		} catch (Exception e) {
			log.error(e);
			throw new ConverterException(e);
		}
	}

	@Override
	public ConciliacaoForPainel convertFrom(final PainelConciliacaoJson json) {
		throw new ConverterException("Conversão de PainelConciliacaoJson para ConciliacaoForExecution não prevista.");
	}
	
	public PainelConciliacaoConverter of (final Date data) {
		this.data = data;
		return this;
	}
}