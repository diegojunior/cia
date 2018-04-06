package br.com.totvs.cia.gateway.amplis.processamento.posicao.adapter;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;

@Component
public class PosicaoAmplisPorDataTarget {
	
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
	
	public List<UnidadeProcessamentoJson> consumir(final Date dataPosicao, final ConfiguracaoServico configuracaoServico){
		if(configuracaoServico.isRendaVariavelAVista()){
			return this.posicaoAVistaAdapter.listBy(dataPosicao);
		}
		
		if(configuracaoServico.isRendaVariavelOpcoes()){
			return this.posicaoOpcoesAdapter.listBy(dataPosicao);
		}
		
		if(configuracaoServico.isRendaVariavelEmprestimo()){
			return this.posicaoEmprestimoAdapter.listBy(dataPosicao);
		}
		
		if(configuracaoServico.isRendaVariavelTermo()){
			return this.posicaoTermoRendaVariavelAdapter.listBy(dataPosicao);
		}
		
		if(configuracaoServico.isRendaVariavelFundoAMercado()){
			return this.posicaoFundoAMercadoRendaVariavelAdapter.listBy(dataPosicao);
		}

		if(configuracaoServico.isDerivativosDisponivel()){
			return this.posicaoDisponivelAdapter.listBy(dataPosicao);
		}
		
		if(configuracaoServico.isDerivativosFuturos()){
			return this.posicaoFuturosAdapter.listBy(dataPosicao);
		}
		
		if(configuracaoServico.isRendaFixaCompromissada()){
			return this.posicaoCompromissadasAdapter.listBy(dataPosicao);
		}
		
		if(configuracaoServico.isRendaFixaDefinitiva()){
			return this.posicaoDefinitivasAdapter.listBy(dataPosicao);
		}
		
		if(configuracaoServico.isRendaFixaTermo()){
			return this.posicaoTermosRendaFixaAdapter.listBy(dataPosicao);
		}
		
		if(configuracaoServico.isFundoInvestimento()){
			return this.posicaoFundosInvestimentosAdapter.listBy(dataPosicao);
		}
		
		if(configuracaoServico.isSwap()){
			return this.posicaoSwapsAdapter.listBy(dataPosicao);
		}
		
		if(configuracaoServico.isPatrimonio()){
			return this.posicaoPatrimonioAdapter.listBy(dataPosicao);
		}		
		
		throw new RuntimeException("Serviço de Posição não identificado.");
	}
}