package br.com.totvs.cia.gateway.amplis.processamento.movimento.adapter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;

@Component
public class MovimentoAmplisPorClienteTarget {
	
	@Autowired
	private MovimentoAmplisRendaVariavelFundoAMercadoAdapter movimentoFundoAMercadoRendaVariavelAdapter;

	public List<UnidadeProcessamentoJson> consumir(final Date dataPosicao, final ConfiguracaoServico configuracaoServico, final String cliente){	
		
		if(configuracaoServico.isMovimentoRVFundoAMercado()){
			return movimentoFundoAMercadoRendaVariavelAdapter.listBy(dataPosicao, cliente);
		}
		
		throw new RuntimeException("Serviço de Movimento não identificado.");
	}
}