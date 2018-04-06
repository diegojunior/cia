package br.com.totvs.cia.gateway.amplis.processamento.posicao.fundos.investimento.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.json.PosicaoFundoInvestimentoJson;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;
import br.com.totvs.cia.gateway.amplis.processamento.posicao.fundos.investimento.converter.PosicaoAmplisFundoInvestimentoConverter;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class PosicaoAmplisFundoInvestimentoService {
	
	private static final Logger log = Logger.getLogger(PosicaoAmplisFundoInvestimentoService.class);
	
	@Autowired
	private AmplisWsClient client;
	
	public List<UnidadeProcessamentoJson> listBy(final String dataPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Fundo de Investimento do Amplis: Data - '%s'", dataPosicao));
			
			List<PosicaoFundoInvestimentoJson> posicoes = this.client.getPosicaoFundoInvestimentoResourceApi().getPosicoes(dataPosicao);
			
			return new PosicaoAmplisFundoInvestimentoConverter(dataPosicao).convertListFrom(posicoes);
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Fundo de Investimento não retornou Posições: data '%s'.", dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Fundo de Investimento para data '%s'.", dataPosicao), e);
		}
	}
	
	public List<UnidadeProcessamentoJson> listBy(final String cliente, final String dataPosicao, final String tipoPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Fundo de Investimento do Amplis: Carteira - '%s' - Data - '%s' - Tipo Posição - '%s'", cliente, dataPosicao, tipoPosicao));
			
			List<PosicaoFundoInvestimentoJson> posicoes = this.client.getPosicaoFundoInvestimentoResourceApi().getPosicao(cliente, dataPosicao, tipoPosicao);
			
			return new PosicaoAmplisFundoInvestimentoConverter(dataPosicao).convertListFrom(posicoes);
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Fundo de Investimento não retornou Posições: '%s' na data '%s'.", cliente, dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Fundo de Investimento para '%s' na data '%s'.", cliente, dataPosicao), e);
		}
	}
}