package br.com.totvs.cia.gateway.amplis.processamento.posicao.swap.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.json.PosicaoSwapJson;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;
import br.com.totvs.cia.gateway.amplis.processamento.posicao.swap.converter.PosicaoAmplisSwapConverter;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class PosicaoAmplisSwapService {
	
	private static final Logger log = Logger.getLogger(PosicaoAmplisSwapService.class);
	
	@Autowired
	private AmplisWsClient client;
	
	public List<UnidadeProcessamentoJson> listBy(final String dataPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Swap do Amplis: Data - '%s'", dataPosicao));
			
			List<PosicaoSwapJson> posicoes = this.client.getPosicaoSwapResourceApi().getPosicoes(dataPosicao);
			
			List<UnidadeProcessamentoJson> unidades = Lists.newArrayList();
			for (PosicaoSwapJson posicao : posicoes) {
				unidades.addAll(new PosicaoAmplisSwapConverter(posicao.getCarteira(), dataPosicao).convertListFrom(posicao.getSwaps()));
			}
			
			return unidades;
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Swap não retornou Posições: data '%s'.", dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Swap para data '%s'.", dataPosicao), e);
		}
	}
	
	public List<UnidadeProcessamentoJson> listBy(final String cliente, final String dataPosicao, final String tipoPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Swap do Amplis: Cliente - '%s' - Data - '%s' - Tipo Posição - '%s'", cliente, dataPosicao, tipoPosicao));
			
			PosicaoSwapJson posicao = this.client.getPosicaoSwapResourceApi().getPosicao(cliente, dataPosicao, tipoPosicao);;

			return new PosicaoAmplisSwapConverter(posicao.getCarteira(), posicao.getDataPosicao()).convertListFrom(posicao.getSwaps());
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Swap não retornou Posições: '%s' na data '%s'.", cliente, dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Swap para '%s' na data '%s'.", cliente, dataPosicao), e);
		}
	}
}