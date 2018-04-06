package br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.avista.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.json.PosicaoAVistaRVJson;
import br.com.totvs.amplis.api.client.json.PosicaoRendaVariavelJson;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;
import br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.avista.converter.PosicaoAmplisRendaVariavelAVistaConverter;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class PosicaoAmplisRendaVariavelAVistaService {
	
	private static final Logger log = Logger.getLogger(PosicaoAmplisRendaVariavelAVistaService.class);
	
	@Autowired
	private AmplisWsClient client;
	
	public List<UnidadeProcessamentoJson> listBy(final String dataPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Renda Variavel A Vista do Amplis: Data - '%s'", dataPosicao));
			
			List<PosicaoAVistaRVJson> posicoes = this.client.getPosicaoRendaVariavelAVistaResourceApi().getPosicoes(dataPosicao);
			
			return new PosicaoAmplisRendaVariavelAVistaConverter(dataPosicao).convertListFrom(posicoes);
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Renda Variável A Vista não retornou Posições: Data '%s'.", dataPosicao));
				return Lists.newArrayList();
			}

			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Renda Variavel A Vista Data '%s'.", dataPosicao), e);
		}
	}
	
	public List<UnidadeProcessamentoJson> listBy(final String cliente, final String dataPosicao, final String tipoPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Renda Variavel A Vista do Amplis: Carteira - '%s' - Data - '%s' - Tipo Posição - '%s'", cliente, dataPosicao, tipoPosicao));

			PosicaoRendaVariavelJson posicao = this.client.getPosicaoRendaVariavelResourceApi().getPosicao(cliente, dataPosicao, tipoPosicao);

			return new PosicaoAmplisRendaVariavelAVistaConverter(dataPosicao).convertListFrom(posicao.getPosicaoAvista());
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Renda Variável A Vista não retornou Posições: '%s' na data '%s'.", cliente, dataPosicao));
				return Lists.newArrayList();
			}

			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Renda Variavel A Vista para '%s' na data '%s'.", cliente, dataPosicao), e);
		}
	}
}