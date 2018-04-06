package br.com.totvs.cia.gateway.amplis.processamento.posicao.patrimonio.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.json.PosicaoPatrimonioJson;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;
import br.com.totvs.cia.gateway.amplis.processamento.posicao.patrimonio.converter.PosicaoAmplisPatrimonioConverter;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class PosicaoAmplisPatrimonioService {
	
	private static final Logger log = Logger.getLogger(PosicaoAmplisPatrimonioService.class);
	
	@Autowired
	private AmplisWsClient client;
	
	public List<UnidadeProcessamentoJson> listBy(final String dataPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Patrimônio do Amplis: Data - '%s'", dataPosicao));
			
			List<PosicaoPatrimonioJson> posicoes = this.client.getPosicaoPatrimonioResourceApi().getPosicoes(dataPosicao);
			
			return Lists.newArrayList(new PosicaoAmplisPatrimonioConverter(dataPosicao).convertListFrom(posicoes));
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Patrimônio não retornou Posições: data '%s'.", dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Patrimônio para data '%s'.", dataPosicao), e);
		}
	}
	
	public List<UnidadeProcessamentoJson> listBy(final String cliente, final String dataPosicao, final String tipoPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Patrimônio do Amplis: Carteira - '%s' - Data - '%s' - Tipo Posição - '%s'", cliente, dataPosicao, tipoPosicao));
			
			PosicaoPatrimonioJson posicao = this.client.getPosicaoPatrimonioResourceApi().get(cliente, dataPosicao, tipoPosicao);
			
			return Lists.newArrayList(new PosicaoAmplisPatrimonioConverter(dataPosicao).convertFrom(posicao));
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Patrimônio não retornou Posições: '%s' na data '%s'.", cliente, dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Patrimônio para '%s' na data '%s'.", cliente, dataPosicao), e);
		}
	}
}