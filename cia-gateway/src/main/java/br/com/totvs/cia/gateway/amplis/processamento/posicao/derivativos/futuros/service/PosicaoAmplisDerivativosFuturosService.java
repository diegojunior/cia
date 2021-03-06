package br.com.totvs.cia.gateway.amplis.processamento.posicao.derivativos.futuros.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.totvs.amplis.api.client.ApiException;
import br.com.totvs.amplis.api.client.json.PosicaoDerivativosJson;
import br.com.totvs.amplis.api.client.json.PosicaoFuturoJson;
import br.com.totvs.cia.gateway.amplis.infra.AmplisWsClient;
import br.com.totvs.cia.gateway.amplis.processamento.posicao.derivativos.futuros.converter.PosicaoAmplisDerivativosFuturoConverter;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class PosicaoAmplisDerivativosFuturosService {
	
	private static final Logger log = Logger.getLogger(PosicaoAmplisDerivativosFuturosService.class);
	
	@Autowired
	private AmplisWsClient client;
	
	public List<UnidadeProcessamentoJson> listBy(final String dataPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Derivativos Futuros do Amplis: Data - '%s'", dataPosicao));
			
			List<PosicaoFuturoJson> posicoes = this.client.getPosicaoDerivativosFuturosResourceApi().getPosicoes(dataPosicao);
			
			return new PosicaoAmplisDerivativosFuturoConverter(dataPosicao).convertListFrom(posicoes);
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Derivativos Futuros não retornou Posições: data '%s'.", dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Detivativos Futuros para data '%s'.", dataPosicao), e);
		}
	}
	
	public List<UnidadeProcessamentoJson> listBy(final String cliente, final String dataPosicao, final String tipoPosicao) {
		try{
			log.info(String.format("Executando GET para obter Posições de Derivativos Futuros do Amplis: Carteira - '%s' - Data - '%s' - Tipo Posição - '%s'", cliente, dataPosicao, tipoPosicao));
			
			PosicaoDerivativosJson posicao = this.client.getPosicaoDerivativosResourceApi().getPosicao(cliente, dataPosicao, tipoPosicao);
			
			return new PosicaoAmplisDerivativosFuturoConverter(dataPosicao).convertListFrom(posicao.getPosicaoFuturo());
		} catch (ApiException e){
			if (e.getCode() >= 400 && e.getCode() < 500) {
				log.warn(String.format("Serviço de Derivativos Futuros não retornou Posições: '%s' na data '%s'.", cliente, dataPosicao));
				return Lists.newArrayList();
			}
			
			log.error(String.format("Mensagem: '%s' - Status: '%s' ", e.getMessage(), e.getCode()));
			throw new GatewayException(String.format("Ocorreu erro ao consultar posições de Detivativos Futuros para '%s' na data '%s'.", cliente, dataPosicao), e);
		}
	}
}