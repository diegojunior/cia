package br.com.totvs.cia.gateway.amplis.processamento.posicao.derivativos.disponivel.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.json.PosicaoDerivativosJson;
import br.com.totvs.amplis.api.client.json.PosicaoDisponivelJson;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;
import br.com.totvs.cia.gateway.amplis.processamento.posicao.derivativos.disponivel.converter.PosicaoAmplisDerivativosDisponivelConverter;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class PosicaoAmplisDerivativosDisponivelService {
	
	private static final Logger log = Logger.getLogger(PosicaoAmplisDerivativosDisponivelService.class);
	
	@Autowired
	private AmplisWsClient client;
	
	public List<UnidadeProcessamentoJson> listBy(final String dataPosicao) {
		try{
			log.debug(String.format("Executando GET para obter Posições de Derivativos Disponível do Amplis: Data - '%s'", dataPosicao));
			
			List<PosicaoDisponivelJson> posicoes = this.client.getPosicaoDerivativosDisponivelResourceApi().getPosicoes(dataPosicao);
			
			return new PosicaoAmplisDerivativosDisponivelConverter(dataPosicao).convertListFrom(posicoes);
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.debug(String.format("Serviço de Derivativos Disponível não retornou Posições: data '%s'.", dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Derivativos Disponivel para data '%s'.", dataPosicao), e);
		}
	}
	
	public List<UnidadeProcessamentoJson> listBy(final String cliente, final String dataPosicao, final String tipoPosicao) {
		try{
			log.debug(String.format("Executando GET para obter Posições de Derivativos Disponível do Amplis: Carteira - '%s' - Data - '%s' - Tipo Posição - '%s'", cliente, dataPosicao, tipoPosicao));
			
			PosicaoDerivativosJson posicao = this.client.getPosicaoDerivativosResourceApi().getPosicao(cliente, dataPosicao, tipoPosicao);
			
			return new PosicaoAmplisDerivativosDisponivelConverter(dataPosicao).convertListFrom(posicao.getPosicaoDisponivel());
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.debug(String.format("Serviço de Derivativos Disponível não retornou Posições: '%s' na data '%s'.", cliente, dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Derivativos Disponivel para '%s' na data '%s'.", cliente, dataPosicao), e);
		}
	}
}