package br.com.totvs.cia.gateway.amplis.processamento.posicao.adapter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.termo.service.PosicaoAmplisRendaVariavelTermoService;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.util.DateUtil;

@Component
public class PosicaoAmplisRendaVariavelTermoAdapter {

	@Autowired
	private PosicaoAmplisRendaVariavelTermoService processamentoAmplisService;

	private static final String FECHAMENTO = "F";
	
	public List<UnidadeProcessamentoJson> listBy(final Date dataPosicao) {
		String data = DateUtil.format(dataPosicao, DateUtil.yyyy_MM_dd);

		return this.processamentoAmplisService.listBy(data);
	}

	public List<UnidadeProcessamentoJson> listBy(final Date dataPosicao, final String cliente) {
		String data = DateUtil.format(dataPosicao, DateUtil.yyyy_MM_dd);

		return this.processamentoAmplisService.listBy(cliente, data, FECHAMENTO);
	}
}