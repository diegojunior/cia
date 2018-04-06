package br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.termo.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.json.PosicaoRendaVariavelJson;
import br.com.totvs.amplis.api.client.json.PosicaoTermoJson;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;
import br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.termo.converter.PosicaoAmplisRendaVariavelTermoConverter;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class PosicaoAmplisRendaVariavelTermoService {
	
	private static final Logger log = Logger.getLogger(PosicaoAmplisRendaVariavelTermoService.class);
	
	@Autowired
	private AmplisWsClient client;
	
	public List<UnidadeProcessamentoJson> listBy(final String dataPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Renda Variavel Termo do Amplis: Data - '%s'", dataPosicao));
			
			List<PosicaoTermoJson> posicoes = this.client.getPosicaoRendaVariavelTermoResourceApi().getPosicoes(dataPosicao);
			
			return new PosicaoAmplisRendaVariavelTermoConverter(dataPosicao).convertListFrom(posicoes);
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Renda Variável de Termo não retornou Posições: data '%s'.", dataPosicao));
				return Lists.newArrayList();
			}

			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Renda Variavel Termo para data '%s'.", dataPosicao), e);
		}
	}
	
	public List<UnidadeProcessamentoJson> listBy(final String cliente, final String dataPosicao, final String tipoPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Renda Variavel Termo do Amplis: Cliente - '%s' - Data - '%s' - Tipo Posição - '%s'", cliente, dataPosicao, tipoPosicao));
			
			PosicaoRendaVariavelJson posicao = this.client.getPosicaoRendaVariavelResourceApi().getPosicao(cliente, dataPosicao, tipoPosicao);
			
			return new PosicaoAmplisRendaVariavelTermoConverter(dataPosicao).convertListFrom(posicao.getPosicaoTermo());
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Renda Variável de Termo não retornou Posições: '%s' na data '%s'.", cliente, dataPosicao));
				return Lists.newArrayList();
			}

			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Renda Variavel Termo para '%s' na data '%s'.", cliente, dataPosicao), e);
		}
	}
}