package br.com.totvs.cia.gateway.amplis.infra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.totvs.amplis.api.client.ApiClient;
import br.com.totvs.amplis.api.client.http.CarteiraResourceApi;
import br.com.totvs.amplis.api.client.http.CorretoraResourceApi;
import br.com.totvs.amplis.api.client.http.DestinatarioRemetenteResourceApi;
import br.com.totvs.amplis.api.client.http.EquivalenciaResourceApi;
import br.com.totvs.amplis.api.client.http.GrupoCarteiraResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoAVistaRVResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoCompromissadaRFResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoDefinitivaRFResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoDerivativosResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoDisponivelDRResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoEmprestimoRVResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoFundoAMercadoResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoFundoInvestimentoResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoFuturoDRResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoOpcoesRVResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoPatrimonioResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoRendaVariavelResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoSwapResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoTermoRFResourceApi;
import br.com.totvs.amplis.api.client.http.PosicaoTermoRVResourceApi;
import br.com.totvs.amplis.api.client.http.TipoEquivalenciaResourceApi;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
public class AmplisWsClient {
	
	@Autowired
	private ApiClient apiClient;
	
	// BASICOS
	public CarteiraResourceApi getCarteiraResourceApi() {
		return new CarteiraResourceApi(this.apiClient);
	}
	
	public GrupoCarteiraResourceApi getGrupoCarteiraResourceApi() {
		return new GrupoCarteiraResourceApi(this.apiClient);
	}

	public CorretoraResourceApi getCorretoraResourceApi() {
		return new CorretoraResourceApi(this.apiClient);
	}
	
	public EquivalenciaResourceApi getEquivalenciaResourceApi() {
		return new EquivalenciaResourceApi(this.apiClient);
	}
	
	public DestinatarioRemetenteResourceApi getDestinatarioRemetenteResourceApi() {
		return new DestinatarioRemetenteResourceApi(this.apiClient);
	}
	
	public TipoEquivalenciaResourceApi getTipoEquivalenciaResourceApi() {
		return new TipoEquivalenciaResourceApi(this.apiClient);
	}
	
	// MOVIMENTOS DE RENDA VARIAVEL
	public PosicaoFundoAMercadoResourceApi getMovimentoFundoAMercadoResourceApi() {
		return new PosicaoFundoAMercadoResourceApi(this.apiClient);
	}
	
	// POSICOES DE RENDA VARIAVEL
	public PosicaoRendaVariavelResourceApi getPosicaoRendaVariavelResourceApi() {
		return new PosicaoRendaVariavelResourceApi(this.apiClient);
	}
	
	public PosicaoAVistaRVResourceApi getPosicaoRendaVariavelAVistaResourceApi() {
		return new PosicaoAVistaRVResourceApi(this.apiClient);
	}
	
	public PosicaoOpcoesRVResourceApi getPosicaoRendaVariavelOpcoesResourceApi() {
		return new PosicaoOpcoesRVResourceApi(this.apiClient);
	}
	
	public PosicaoEmprestimoRVResourceApi getPosicaoRendaVariavelEmprestimoResourceApi() {
		return new PosicaoEmprestimoRVResourceApi(this.apiClient);
	}
	
	public PosicaoTermoRVResourceApi getPosicaoRendaVariavelTermoResourceApi() {
		return new PosicaoTermoRVResourceApi(this.apiClient);
	}
	
	public PosicaoFundoAMercadoResourceApi getPosicaoFundoAMercadoResourceApi() {
		return new PosicaoFundoAMercadoResourceApi(this.apiClient);
	}
	
	// POSICOES DE DERIVATIVOS
	public PosicaoDerivativosResourceApi getPosicaoDerivativosResourceApi() {
		return new PosicaoDerivativosResourceApi(this.apiClient);
	}
	
	public PosicaoDisponivelDRResourceApi getPosicaoDerivativosDisponivelResourceApi() {
		return new PosicaoDisponivelDRResourceApi(this.apiClient);
	}
	
	public PosicaoFuturoDRResourceApi getPosicaoDerivativosFuturosResourceApi() {
		return new PosicaoFuturoDRResourceApi(this.apiClient);
	}
	
	// POSICOES DE RENDA FIXA
	public PosicaoCompromissadaRFResourceApi getPosicaoCompromissadaRFResourceApi() {
		return new PosicaoCompromissadaRFResourceApi(this.apiClient);
	}
	
	public PosicaoDefinitivaRFResourceApi getPosicaoDefinitivaRFResourceApi() {
		return new PosicaoDefinitivaRFResourceApi(this.apiClient);
	}
	
	public PosicaoTermoRFResourceApi getPosicaoTermoRFResourceApi() {
		return new PosicaoTermoRFResourceApi(this.apiClient);
	}
	
	// POSICOES DE SWAP
	public PosicaoSwapResourceApi getPosicaoSwapResourceApi() {
		return new PosicaoSwapResourceApi(this.apiClient);
	}
	
	// POSICOES DE FUNDOS
	public PosicaoFundoInvestimentoResourceApi getPosicaoFundoInvestimentoResourceApi() {
		return new PosicaoFundoInvestimentoResourceApi(this.apiClient);
	}
	
	// POSICOES DE PATRIMONIO
	public PosicaoPatrimonioResourceApi getPosicaoPatrimonioResourceApi() {
		return new PosicaoPatrimonioResourceApi(this.apiClient);
	}
}