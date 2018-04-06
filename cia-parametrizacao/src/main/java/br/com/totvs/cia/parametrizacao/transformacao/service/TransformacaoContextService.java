package br.com.totvs.cia.parametrizacao.transformacao.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.converter.EquivalenciaConverter;
import br.com.totvs.cia.gateway.core.equivalencia.service.EquivalenciaGatewayService;
import br.com.totvs.cia.parametrizacao.transformacao.model.TipoTransformacaoEnum;
import br.com.totvs.cia.parametrizacao.transformacao.model.Transformacao;
import br.com.totvs.cia.parametrizacao.transformacao.model.TransformacaoStrategy;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TransformacaoContextService {
	
	@Autowired
	private EquivalenciaGatewayService equivalenciaGatewayService;

	@Autowired
	private EquivalenciaConverter equivalenciaConverter;
	
	public TransformacaoStrategy loadStrategies(final SistemaEnum sistema, final Transformacao transformacao) {
		final TransformacaoStrategy strategy = this.getInstance(transformacao.getTipoTransformacao());
		if (strategy == null) {
			throw new UnsupportedOperationException("Deve-se registrar a estrategia para o tipo de transformação - " + transformacao.getTipoTransformacao());
		}
		strategy.loadItens(sistema, transformacao);
		return strategy;
	}
	
	private TransformacaoStrategy getInstance(final TipoTransformacaoEnum tipo) {
		Map<TipoTransformacaoEnum, TransformacaoStrategy> strategies = Maps.newHashMap();
		strategies.put(TipoTransformacaoEnum.FIXO, this.getTransformacaoFixaStrategyInstance());
		strategies.put(TipoTransformacaoEnum.EQUIVALENCIA, this.getTransformacaoEquivalenciaStrategyInstance());
		return strategies.get(tipo);
	}
	
	@Bean
	private TransformacaoFixoStrategy getTransformacaoFixaStrategyInstance() {
		return new TransformacaoFixoStrategy();
	}
	
	@Bean
	private TransformacaoEquivalenciaStrategy getTransformacaoEquivalenciaStrategyInstance() {
		return new TransformacaoEquivalenciaStrategy(this.equivalenciaGatewayService, this.equivalenciaConverter);
	}
}