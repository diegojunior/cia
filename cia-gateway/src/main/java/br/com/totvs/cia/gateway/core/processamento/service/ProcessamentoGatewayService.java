package br.com.totvs.cia.gateway.core.processamento.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.gateway.amplis.processamento.movimento.adapter.MovimentoAmplisPorClienteTarget;
import br.com.totvs.cia.gateway.amplis.processamento.movimento.adapter.MovimentoAmplisPorDataTarget;
import br.com.totvs.cia.gateway.amplis.processamento.posicao.adapter.PosicaoAmplisPorClienteTarget;
import br.com.totvs.cia.gateway.amplis.processamento.posicao.adapter.PosicaoAmplisPorDataTarget;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.integracao.core.exception.GatewayException;

@Service
public class ProcessamentoGatewayService {
	
	@Autowired
	private MovimentoAmplisPorClienteTarget movimentoAmplisPorCliente;
	
	@Autowired
	private MovimentoAmplisPorDataTarget movimentoAmplisPorData;
	
	@Autowired
	private PosicaoAmplisPorClienteTarget posicaoAmplisPorCliente;
	
	@Autowired
	private PosicaoAmplisPorDataTarget posicaoAmplisPorData;
	
	public List<UnidadeProcessamentoJson> listBy(final Date dataPosicao, final ConfiguracaoServico configuracaoServico){
		if(configuracaoServico.getSistema().isAmplis()) {
			if (configuracaoServico.isTipoPosicao()){
				return this.posicaoAmplisPorData.consumir(dataPosicao, configuracaoServico);
			}
			if (configuracaoServico.isTipoMovimento()) {
				return this.movimentoAmplisPorData.consumir(dataPosicao, configuracaoServico);
			}
			throw new GatewayException("Tipo de Serviço não identificado");
		}
		throw new GatewayException("Sistema não identificado");
	}
	
	public List<UnidadeProcessamentoJson> listBy(final Date dataPosicao, final ConfiguracaoServico configuracaoServico, final String cliente){
		if(configuracaoServico.getSistema().isAmplis()) {
			if (configuracaoServico.isTipoPosicao()){
				return this.posicaoAmplisPorCliente.consumir(dataPosicao, configuracaoServico, cliente);
			}
			if (configuracaoServico.isTipoMovimento()) {
				return this.movimentoAmplisPorCliente.consumir(dataPosicao, configuracaoServico, cliente);
			}
			throw new GatewayException("Tipo de Serviço não identificado");
		}
		throw new GatewayException("Sistema não identificado");
	}
}