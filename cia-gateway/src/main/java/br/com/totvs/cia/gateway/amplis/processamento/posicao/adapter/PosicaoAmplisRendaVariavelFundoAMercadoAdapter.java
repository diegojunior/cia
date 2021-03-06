package br.com.totvs.cia.gateway.amplis.processamento.posicao.adapter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.fundoamercado.service.PosicaoAmplisRendaVariavelFundoAMercadoService;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.util.DateUtil;

@Component
public class PosicaoAmplisRendaVariavelFundoAMercadoAdapter {
	
	private static final String FECHAMENTO = "F";

	@Autowired
	private PosicaoAmplisRendaVariavelFundoAMercadoService processamentoRendaVariavelFundoAMercadoAmplisService;
	
	public List<UnidadeProcessamentoJson> listBy(final Date dataPosicao) {
		String data = DateUtil.format(dataPosicao, DateUtil.yyyy_MM_dd);
		
		return this.processamentoRendaVariavelFundoAMercadoAmplisService.listBy(data);
	}
	
	public List<UnidadeProcessamentoJson> listBy(final Date dataPosicao, final String cliente){
		String data = DateUtil.format(dataPosicao, DateUtil.yyyy_MM_dd);
		
		return this.processamentoRendaVariavelFundoAMercadoAmplisService.listBy(cliente, data, FECHAMENTO);
	}
}