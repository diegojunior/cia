package br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.fundoamercado.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.json.PosicaoFundoAMercadoJson;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;
import br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.fundoamercado.converter.PosicaoAmplisRendaVariavelFundoAMercadoConverter;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class PosicaoAmplisRendaVariavelFundoAMercadoService {
	
	private static final Logger log = Logger.getLogger(PosicaoAmplisRendaVariavelFundoAMercadoService.class);
	
	@Autowired
	private AmplisWsClient client;
	
	public List<UnidadeProcessamentoJson> listBy(final String dataPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Renda Variavel Fundo A Mercado do Amplis: Data - '%s'", dataPosicao));
			
			List<PosicaoFundoAMercadoJson> posicoes = this.client.getPosicaoFundoAMercadoResourceApi().getPosicoes(dataPosicao);
			
			return new PosicaoAmplisRendaVariavelFundoAMercadoConverter(dataPosicao).convertListFrom(posicoes);
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Renda Variável Fundo A Mercado não retornou Posições: Data '%s'.", dataPosicao));
				return Lists.newArrayList();
			}

			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Renda Variavel Fundo A Mercado. Data '%s'.", dataPosicao), e);
		}
	}
	
	public List<UnidadeProcessamentoJson> listBy(final String cliente, final String dataPosicao, final String tipoPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Renda Variavel Fundo A Mercado do Amplis: Carteira - '%s' - Data - '%s' - Tipo Posição - '%s'", cliente, dataPosicao, tipoPosicao));

			List<PosicaoFundoAMercadoJson> posicoes = this.client.getPosicaoFundoAMercadoResourceApi().getPosicao(cliente, dataPosicao, tipoPosicao);

			return new PosicaoAmplisRendaVariavelFundoAMercadoConverter(dataPosicao).convertListFrom(posicoes);
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Renda Variável Fundo A Mercado não retornou Posições: '%s' na data '%s'.", cliente, dataPosicao));
				return Lists.newArrayList();
			}

			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Renda Variavel Fundo A Mercado para '%s' na data '%s'.", cliente, dataPosicao), e);
		}
	}
}