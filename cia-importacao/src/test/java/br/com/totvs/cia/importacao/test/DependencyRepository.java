package br.com.totvs.cia.importacao.test;

import java.util.List;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;

import br.com.totvs.cia.cadastro.agente.repository.AgenteRepository;
import br.com.totvs.cia.cadastro.equivalencia.repository.EquivalenciaRepository;
import br.com.totvs.cia.importacao.job.ImportacaoItemBlocoReader;
import br.com.totvs.cia.importacao.mock.ImportacaoMock;
import br.com.totvs.cia.importacao.mock.UnidadeLayoutImportacaoMock;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.importacao.repository.ArquivoRepository;
import br.com.totvs.cia.importacao.repository.ImportacaoRepository;
import br.com.totvs.cia.importacao.repository.UnidadeImportacaoCustomRepository;
import br.com.totvs.cia.importacao.repository.UnidadeImportacaoRepository;
import br.com.totvs.cia.importacao.repository.UnidadeLayoutImportacaoCustomRepository;
import br.com.totvs.cia.importacao.repository.UnidadeLayoutImportacaoRepository;
import br.com.totvs.cia.notificacao.repository.NotificacaoImportacaoRepository;
import br.com.totvs.cia.parametrizacao.dominio.repository.DominioRepository;
import br.com.totvs.cia.parametrizacao.layout.repository.SessaoRepository;
import br.com.totvs.cia.parametrizacao.mock.DominioMock;
import br.com.totvs.cia.parametrizacao.mock.ParametrizacaoUnidadeImportacaoBlocoMock;
import br.com.totvs.cia.parametrizacao.mock.ParametrizacaoUnidadeImportacaoChaveMock;
import br.com.totvs.cia.parametrizacao.transformacao.repository.TransformacaoRepository;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.ParametrizacaoUnidadeImportacaoBlocoRepository;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.ParametrizacaoUnidadeImportacaoChaveRepository;
import br.com.totvs.cia.parametrizacao.unidade.importacao.repository.ParametrizacaoUnidadeImportacaoChaveSpecification;
import br.com.totvs.cia.parametrizacao.validacao.repository.ValidacaoArquivoRepository;

@Configuration
public class DependencyRepository {

	@Bean
	public AgenteRepository getAgenteRepository() {
		return Mockito.mock(AgenteRepository.class);
	}
	
	@Bean
	public DominioRepository getDominioRepository() {
		DominioRepository mock = Mockito.mock(DominioRepository.class);
		Mockito.when(mock.findAll()).thenReturn(DominioMock.allMocks());
		return mock;
	}
	
	@Bean
	public SessaoRepository getSessaoRepository() {
		SessaoRepository mock = Mockito.mock(SessaoRepository.class);
		return mock;
	}
	
	@Bean
	public ArquivoRepository getArquivoRepository() {
		ArquivoRepository mock = Mockito.mock(ArquivoRepository.class);
		return mock;
	}
	
	@Bean
	public ImportacaoRepository getImportacaoRepository() {
		ImportacaoRepository mock = Mockito.mock(ImportacaoRepository.class);
		Importacao importacaoDelimitadorPontoVirgulaMock = ImportacaoMock.mockDelimitadorPontoVirgula();
		Importacao mockPosicionalCinfTamanhoInvalido = ImportacaoMock.mockPosicionalCinfTamanhoInvalido();
		Importacao importacaoPosicionalMock = ImportacaoMock.mockPosicionalCinf();
		Importacao importacaoXml = ImportacaoMock.mockXml();
		Importacao importacaoXmlComParametrizacaoUnidade = ImportacaoMock.mockXmlComParametrizacaoUnidade();
		Mockito.when(mock.findOne("123")).thenReturn(importacaoDelimitadorPontoVirgulaMock);
		Mockito.when(mock.findOne("124")).thenReturn(importacaoPosicionalMock);
		Mockito.when(mock.findOne("125")).thenReturn(importacaoXml);
		Mockito.when(mock.findOne("126")).thenReturn(mockPosicionalCinfTamanhoInvalido);
		Mockito.when(mock.findOne("127")).thenReturn(importacaoXmlComParametrizacaoUnidade);
		Mockito.when(mock.getOne("123")).thenReturn(importacaoDelimitadorPontoVirgulaMock);
		Mockito.when(mock.getOne("124")).thenReturn(importacaoPosicionalMock);
		Mockito.when(mock.getOne("125")).thenReturn(importacaoXml);
		Mockito.when(mock.getOne("126")).thenReturn(mockPosicionalCinfTamanhoInvalido);
		Mockito.when(mock.getOne("127")).thenReturn(importacaoXmlComParametrizacaoUnidade);
		
		Mockito.when(mock.loadImportacaoAndUnidades("123")).thenReturn(importacaoDelimitadorPontoVirgulaMock);
		Mockito.when(mock.loadImportacaoAndUnidades("124")).thenReturn(importacaoPosicionalMock);
		Mockito.when(mock.loadImportacaoAndUnidades("125")).thenReturn(importacaoXml);
		Mockito.when(mock.loadImportacaoAndUnidades("126")).thenReturn(mockPosicionalCinfTamanhoInvalido);
		Mockito.when(mock.loadImportacaoAndUnidades("127")).thenReturn(importacaoXmlComParametrizacaoUnidade);
		return mock;
	}
	
	@Bean
	public UnidadeImportacaoRepository getUnidadeImportacaoRepository() {
		return Mockito.mock(UnidadeImportacaoRepository.class);
	}
	
	@Bean
	public UnidadeImportacaoCustomRepository getUnidadeImportacaoCustomRepository() {
		return Mockito.mock(UnidadeImportacaoCustomRepository.class);
	}
	
	@Bean
	public NotificacaoImportacaoRepository getNotificacaoImportacaoRepository() {
		return Mockito.mock(NotificacaoImportacaoRepository.class);
	}
	
	@Bean
	public ValidacaoArquivoRepository getValidacaoArquivoRepository() {
		return Mockito.mock(ValidacaoArquivoRepository.class);
	}
	
	@Bean
	public UnidadeLayoutImportacaoRepository getUnidadeLayoutImportacaoRepository() {
		UnidadeLayoutImportacaoRepository mock = Mockito.mock(UnidadeLayoutImportacaoRepository.class);
		Importacao importacaoDelimitador = this.getImportacaoRepository().findOne("123");
		Importacao importacaoPosicional = this.getImportacaoRepository().findOne("124");
		Importacao importacaoXml = this.getImportacaoRepository().findOne("125");
		Importacao importacaoXmlComParametrizacao = this.getImportacaoRepository().findOne("127");
		
		PageRequest pageRequest = new PageRequest(0, ImportacaoItemBlocoReader.SIZE);
		Mockito
			.when(mock.obtemUnidadesOrdenadasPeloNumeroLinha(importacaoDelimitador, pageRequest))
			.thenReturn(UnidadeLayoutImportacaoMock.mockUnidadesImportacaoDelimitador());
		Mockito
			.when(mock.getUnidadesByImportacao(importacaoPosicional, pageRequest))
			.thenReturn(UnidadeLayoutImportacaoMock.mockUnidadesImportacaoCinfPosicional());
		Mockito
			.when(mock.getUnidadesByImportacao(importacaoXml, pageRequest))
			.thenReturn(UnidadeLayoutImportacaoMock.mockUnidadeImportacaoXml());
		Mockito
			.when(mock.getUnidadesByImportacao(importacaoXmlComParametrizacao, pageRequest))
			.thenReturn(UnidadeLayoutImportacaoMock.mockUnidadeImportacaoXml());
		return mock;
	}
	
	@Bean
	public UnidadeLayoutImportacaoCustomRepository getUnidadeLayoutImportacaoCustomRepository() {
		return Mockito.mock(UnidadeLayoutImportacaoCustomRepository.class);
	}
	
	@Bean
	public ParametrizacaoUnidadeImportacaoChaveRepository getParametrizacaoUnidadeImportacaoRepository() {
		Importacao importacaoXmlComParametrizacao = ImportacaoMock.mockXmlComParametrizacaoUnidade();
		ParametrizacaoUnidadeImportacaoChaveRepository mock = Mockito.mock(ParametrizacaoUnidadeImportacaoChaveRepository.class);
		Mockito
			.when(mock.findAll(ParametrizacaoUnidadeImportacaoChaveSpecification.findByLayout(importacaoXmlComParametrizacao.getLayout().getId())))
			.thenReturn(ParametrizacaoUnidadeImportacaoChaveMock.mock());
		return mock;
	}
	
	@Bean
	public TransformacaoRepository getTransformacaoRepository() {
		return Mockito.mock(TransformacaoRepository.class);
	}
	
	@Bean
	public ParametrizacaoUnidadeImportacaoBlocoRepository getParametrizacaoUnidadeBlocoImportacaoRepository() {
		ParametrizacaoUnidadeImportacaoBlocoRepository mock = Mockito.mock(ParametrizacaoUnidadeImportacaoBlocoRepository.class);
		List<ParametrizacaoUnidadeImportacaoBloco> parametrizacao = ParametrizacaoUnidadeImportacaoBlocoMock.mockParametrizacaoDelimitador();
		Mockito.when(mock.findByLayout("1")).thenReturn(parametrizacao);
		return mock;
	}
	
	@Bean
	public EquivalenciaRepository getEquivalenciaRepository() {
		return Mockito.mock(EquivalenciaRepository.class);
	}
	
}
