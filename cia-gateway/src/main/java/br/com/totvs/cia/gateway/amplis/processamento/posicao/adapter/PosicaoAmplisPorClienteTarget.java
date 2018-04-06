package br.com.totvs.cia.gateway.amplis.processamento.posicao.adapter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;

@Component
public class PosicaoAmplisPorClienteTarget {
	
	@Autowired
	private PosicaoAmplisRendaVariavelAVistaAdapter posicaoAVistaAdapter;
	
	@Autowired
	private PosicaoAmplisRendaVariavelOpcoesAdapter posicaoOpcoesAdapter;
	
	@Autowired
	private PosicaoAmplisRendaVariavelEmprestimoAdapter posicaoEmprestimoAdapter;
	
	@Autowired
	private PosicaoAmplisRendaVariavelTermoAdapter posicaoTermoRendaVariavelAdapter;
	
	@Autowired
	private PosicaoAmplisRendaVariavelFundoAMercadoAdapter posicaoFundoAMercadoRendaVariavelAdapter;
	
	@Autowired
	private PosicaoAmplisDerivativosDisponivelAdapter posicaoDisponivelAdapter;
	
	@Autowired
	private PosicaoAmplisDerivativosFuturosAdapter posicaoFuturosAdapter;
	
	@Autowired
	private PosicaoAmplisRendaFixaCompromissadaAdapter posicaoCompromissadasAdapter;
	
	@Autowired
	private PosicaoAmplisRendaFixaDefinitivaAdapter posicaoDefinitivasAdapter;
	
	@Autowired
	private PosicaoAmplisRendaFixaTermoAdapter posicaoTermosRendaFixaAdapter;
	
	@Autowired
	private PosicaoAmplisFundoInvestimentoAdapter posicaoFundosInvestimentosAdapter;
	
	@Autowired
	private PosicaoAmplisSwapAdapter posicaoSwapsAdapter;
	
	@Autowired
	private PosicaoAmplisPatrimonioAdapter posicaoPatrimonioAdapter;
	
	public List<UnidadeProcessamentoJson> consumir(final Date dataPosicao, final ConfiguracaoServico configuracaoServico, final String cliente){
		if(configuracaoServico.isRendaVariavelAVista()){
			return posicaoAVistaAdapter.listBy(dataPosicao, cliente);
		}
		
		if(configuracaoServico.isRendaVariavelOpcoes()){
			return posicaoOpcoesAdapter.listBy(dataPosicao, cliente);
		}
		
		if(configuracaoServico.isRendaVariavelEmprestimo()){
			return posicaoEmprestimoAdapter.listBy(dataPosicao, cliente);
		}
		
		if(configuracaoServico.isRendaVariavelTermo()){
			return posicaoTermoRendaVariavelAdapter.listBy(dataPosicao, cliente);
		}
		
		if(configuracaoServico.isRendaVariavelFundoAMercado()){
			return posicaoFundoAMercadoRendaVariavelAdapter.listBy(dataPosicao, cliente);
		}

		if(configuracaoServico.isDerivativosDisponivel()){
			return posicaoDisponivelAdapter.listBy(dataPosicao, cliente);
		}
		
		if(configuracaoServico.isDerivativosFuturos()){
			return posicaoFuturosAdapter.listBy(dataPosicao, cliente);
		}
		
		if(configuracaoServico.isRendaFixaCompromissada()){
			return posicaoCompromissadasAdapter.listBy(dataPosicao, cliente);
		}
		
		if(configuracaoServico.isRendaFixaDefinitiva()){
			return posicaoDefinitivasAdapter.listBy(dataPosicao, cliente);
		}
		
		if(configuracaoServico.isRendaFixaTermo()){
			return posicaoTermosRendaFixaAdapter.listBy(dataPosicao, cliente);
		}
		
		if(configuracaoServico.isFundoInvestimento()){
			return posicaoFundosInvestimentosAdapter.listBy(dataPosicao, cliente);
		}
		
		if(configuracaoServico.isSwap()){
			return posicaoSwapsAdapter.listBy(dataPosicao, cliente);
		}
		
		if(configuracaoServico.isPatrimonio()){
			return posicaoPatrimonioAdapter.listBy(dataPosicao, cliente);
		}		
		
		throw new RuntimeException("Serviço de Posição não identificado.");
	}
}