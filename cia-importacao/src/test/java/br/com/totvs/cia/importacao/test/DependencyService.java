package br.com.totvs.cia.importacao.test;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Lists;

import br.com.totvs.amplis.api.client.ApiClient;
import br.com.totvs.cia.cadastro.agente.service.AgenteService;
import br.com.totvs.cia.cadastro.equivalencia.converter.EquivalenciaConverter;
import br.com.totvs.cia.cadastro.equivalencia.service.EquivalenciaService;
import br.com.totvs.cia.gateway.amplis.equivalencia.converter.EquivalenciaGatewayConverter;
import br.com.totvs.cia.gateway.amplis.equivalencia.service.EquivalenciaAmplisService;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;
import br.com.totvs.cia.gateway.core.equivalencia.service.EquivalenciaGatewayService;
import br.com.totvs.cia.importacao.job.TransformacaoExecutor;
import br.com.totvs.cia.importacao.mock.ImportacaoMock;
import br.com.totvs.cia.notificacao.service.NotificacaoImportacaoService;
import br.com.totvs.cia.parametrizacao.dominio.service.DominioService;
import br.com.totvs.cia.parametrizacao.layout.service.SessaoService;
import br.com.totvs.cia.parametrizacao.mock.ParametrizacaoUnidadeImportacaoChaveMock;
import br.com.totvs.cia.parametrizacao.transformacao.service.ItemTransformacaoService;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoContextService;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoEquivalenciaStrategy;
import br.com.totvs.cia.parametrizacao.transformacao.service.TransformacaoService;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoBlocoService;
import br.com.totvs.cia.parametrizacao.unidade.importacao.service.ParametrizacaoUnidadeImportacaoChaveService;
import br.com.totvs.cia.parametrizacao.validacao.service.ValidacaoArquivoService;

@Configuration
public class DependencyService {
	
	//Converter
	@Bean
	public EquivalenciaGatewayConverter getEquivalenciaGatewayConverter() {
		return Mockito.mock(EquivalenciaGatewayConverter.class);
	}
	
	@Bean
	public EquivalenciaConverter getEquivalenciaConverter() {
		return Mockito.mock(EquivalenciaConverter.class);
	}
	
	//Services

	@Bean
	public SessaoService getSessaoService() {
		return new SessaoService();
	}
	
	@Bean
	public DominioService getDominioService() {
		return new DominioService();
	}
	
	@Bean
	public AgenteService getAgenteService() {
		return Mockito.mock(AgenteService.class);
	}
	
	@Bean
	public NotificacaoImportacaoService getNotificacaoImportacaoService() {
		return Mockito.mock(NotificacaoImportacaoService.class);
	}
	
	@Bean
	public ValidacaoArquivoService getValidacaoArquivoService() {
		ValidacaoArquivoService mock = Mockito.mock(ValidacaoArquivoService.class);
		Mockito
			.when(mock.search(Mockito.any(), 
				Mockito.any(), 
				Mockito.any(), 
				Mockito.any(), 
				Mockito.any()))
			.thenReturn(Lists.newArrayList());
		return mock;
	}
	
	@Bean
	public ParametrizacaoUnidadeImportacaoChaveService getParametrizacaoUnidadeImportacaoChaveService() {
		ParametrizacaoUnidadeImportacaoChaveService mock = Mockito.mock(ParametrizacaoUnidadeImportacaoChaveService.class);
		Mockito
			.when(mock.getByLayout(ImportacaoMock.mockXmlComParametrizacaoUnidade().getLayout().getId()))
			.thenReturn(ParametrizacaoUnidadeImportacaoChaveMock.mock());
		return mock;
	}
	
	@Bean
	public TransformacaoService getTransformacaoService() {
		return Mockito.mock(TransformacaoService.class);
	}
	
	@Bean
	public ItemTransformacaoService getItemTransformacaoService() {
		return Mockito.mock(ItemTransformacaoService.class);
	}
	
	@Bean
	public TransformacaoContextService getTransformacaoContextService() {
		return Mockito.mock(TransformacaoContextService.class);
	}
	
	@Bean
	public TransformacaoExecutor getTransformacaoExecutor() {
		return Mockito.mock(TransformacaoExecutor.class);
	}
	
	@Bean
	public TransformacaoEquivalenciaStrategy getTransformacaoEquivalenciaStrategy() {
		return Mockito.mock(TransformacaoEquivalenciaStrategy.class);
	}
	
	@Bean
	public EquivalenciaService getEquivalenciaService() {
		return Mockito.mock(EquivalenciaService.class);
	}
	
	@Bean
	public EquivalenciaGatewayService getEquivalenciaGatewayService() {
		return Mockito.mock(EquivalenciaGatewayService.class);
	}
	
	@Bean
	public EquivalenciaAmplisService getEquivalenciaAmplisService() {
		return Mockito.mock(EquivalenciaAmplisService.class);
	}
	
	@Bean
	public AmplisWsClient getAmplisWsClient() {
		return Mockito.mock(AmplisWsClient.class);
	}
	
	@Bean
	public ApiClient getApiClient() {
		return Mockito.mock(ApiClient.class);
	}
	
	@Bean
	public ParametrizacaoUnidadeImportacaoBlocoService getParametrizacaoUnidadeBlocoImportacaoChaveService() {
		return new ParametrizacaoUnidadeImportacaoBlocoService();
	}
}