package br.com.totvs.cia.parametrizacao.transformacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.converter.EquivalenciaConverter;
import br.com.totvs.cia.cadastro.equivalencia.json.EquivalenciaJson;
import br.com.totvs.cia.cadastro.equivalencia.model.Equivalencia;
import br.com.totvs.cia.cadastro.equivalencia.model.EquivalenciaTable;
import br.com.totvs.cia.gateway.core.equivalencia.service.EquivalenciaGatewayService;
import br.com.totvs.cia.parametrizacao.transformacao.model.ItemTransformacaoEquivalencia;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;
import br.com.totvs.cia.parametrizacao.transformacao.model.TransformacaoStrategy;

public class TransformacaoEquivalenciaStrategy implements TransformacaoStrategy {

	private EquivalenciaGatewayService equivalenciaGatewayService;

	private EquivalenciaConverter equivalenciaConverter;
	
	private EquivalenciaTable equivalenciaTable;
	
	@Autowired
	public TransformacaoEquivalenciaStrategy(final EquivalenciaGatewayService equivalenciaGatewayService,
			final EquivalenciaConverter equivalenciaConverter) {
		this.equivalenciaGatewayService = equivalenciaGatewayService;
		this.equivalenciaConverter = equivalenciaConverter;
	}

	@Override
	public void loadItens(final SistemaEnum sistema, final Transformacao transformacao) {
		if (sistema == null) {
			this.equivalenciaTable = new EquivalenciaTable(Lists.newArrayList());
		} else {
			final ItemTransformacaoEquivalencia itemTransformacao = (ItemTransformacaoEquivalencia) transformacao.getItem();
			final List<EquivalenciaJson> equivalenciasJson = this.equivalenciaGatewayService.getEquivalenciasBy(sistema, itemTransformacao.getRemetente().getCodigo(), itemTransformacao.getTipoEquivalencia().getIdLegado());
			final List<Equivalencia> equivalencias = this.equivalenciaConverter.convertListJsonFrom(equivalenciasJson);
			this.equivalenciaTable = new EquivalenciaTable(equivalencias);
		}
	}


	@Override
	public String transform(final String valor) {
		final String codigoInterno = this.equivalenciaTable.getCodigoInternoPor(valor);
		return codigoInterno != null ? codigoInterno : valor;
	}
}