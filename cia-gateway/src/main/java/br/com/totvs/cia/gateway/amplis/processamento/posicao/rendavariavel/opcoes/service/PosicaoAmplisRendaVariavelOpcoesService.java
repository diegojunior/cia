package br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.opcoes.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.json.PosicaoOpcaoRVJson;
import br.com.totvs.amplis.api.client.json.PosicaoRendaVariavelJson;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;
import br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.opcoes.converter.PosicaoAmplisRendaVariavelOpcoesConverter;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class PosicaoAmplisRendaVariavelOpcoesService {
	
	private static final Logger log = Logger.getLogger(PosicaoAmplisRendaVariavelOpcoesService.class);
	
	@Autowired
	private AmplisWsClient client;
	
	public List<UnidadeProcessamentoJson> listBy(final String dataPosicao) {
		try{
			log.info("Executando GET para obter posições de Renda Variavel de Opções do Amplis.");
			
			List<PosicaoOpcaoRVJson> posicoes = this.client.getPosicaoRendaVariavelOpcoesResourceApi().getPosicoes(dataPosicao);
			
			return new PosicaoAmplisRendaVariavelOpcoesConverter(dataPosicao).convertListFrom(posicoes);
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Renda Variável de Opções não retornou Posições: Data '%s'.", dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Renda Variavel de Opções para data '%s'.", dataPosicao), e);
		}
	}
	
	public List<UnidadeProcessamentoJson> listBy(final String cliente, final String dataPosicao, final String tipoPosicao) {
		try{
			log.info("Executando GET para obter posições de Renda Variavel de Opções do Amplis.");
			
			PosicaoRendaVariavelJson posicao = this.client.getPosicaoRendaVariavelResourceApi().getPosicao(cliente, dataPosicao, tipoPosicao);
			
			return new PosicaoAmplisRendaVariavelOpcoesConverter(dataPosicao).convertListFrom(posicao.getPosicaoOpcao());
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Renda Variável de Opções não retornou Posições: '%s' na data '%s'.", cliente, dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Renda Variavel de Opções para '%s' na data '%s'.", cliente, dataPosicao), e);
		}
	}
}