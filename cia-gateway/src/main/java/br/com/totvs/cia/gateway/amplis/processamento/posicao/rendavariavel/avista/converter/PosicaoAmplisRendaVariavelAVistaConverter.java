package br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.avista.converter;

import static br.com.totvs.cia.infra.util.DateUtil.transformToYYYYMMDD;
import static br.com.totvs.cia.infra.util.NumberUtil.replaceDotToCommaAndFormat;

import br.com.totvs.amplis.api.client.json.PosicaoAVistaRVJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.exception.ConverterException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PosicaoAmplisRendaVariavelAVistaConverter extends JsonGatewayConverter<PosicaoAVistaRVJson, UnidadeProcessamentoJson> {
	
	private String dataPosicao;
	
	public PosicaoAmplisRendaVariavelAVistaConverter(final String dataPosicao) {
		this.dataPosicao = dataPosicao;
	}
	
	@Override
	public UnidadeProcessamentoJson convertFrom(final PosicaoAVistaRVJson posicao) {
		try {
			UnidadeProcessamentoJson unidade = new UnidadeProcessamentoJson(ServicoEnum.RENDAVARIAVEL_AVISTA, this.dataPosicao, posicao.getCarteira() != null ? posicao.getCarteira().getCodigo() : "");
			
			unidade.add(new CampoProcessamentoJson("carteira", posicao.getCarteira() != null ? posicao.getCarteira().getCodigo(): ""));
			unidade.add(new CampoProcessamentoJson("codigoCblc", posicao.getCarteira() != null ? posicao.getCarteira().getCarteiraBase().getCodigoCblc() : ""));
			unidade.add(new CampoProcessamentoJson("dataPosicao", transformToYYYYMMDD(this.dataPosicao)));
			unidade.add(new CampoProcessamentoJson("tipoMercado", posicao.getTipoMercado()));
			unidade.add(new CampoProcessamentoJson("cotacao", replaceDotToCommaAndFormat(posicao.getCotacao())));
			unidade.add(new CampoProcessamentoJson("custoMedioUnitCorr", replaceDotToCommaAndFormat(posicao.getCustoMedioUnitCorr())));
			unidade.add(new CampoProcessamentoJson("qtdDisponivel", posicao.getQtdDisponivel().toString()));
			unidade.add(new CampoProcessamentoJson("qtdTotal", posicao.getQtdTotal().toString()));
			unidade.add(new CampoProcessamentoJson("resultado", replaceDotToCommaAndFormat((posicao.getResultado()))));
			unidade.add(new CampoProcessamentoJson("vlBruto", replaceDotToCommaAndFormat((posicao.getVlBruto()))));
			unidade.add(new CampoProcessamentoJson("vlCustoTotalCorr", replaceDotToCommaAndFormat((posicao.getVlCustoTotalCorr()))));
			unidade.add(new CampoProcessamentoJson("vlLiquido", replaceDotToCommaAndFormat((posicao.getVlLiquido()))));
			
			unidade.add(new CampoProcessamentoJson("codigoAtivo", posicao.getAtivo().getCodigo()));
			unidade.add(new CampoProcessamentoJson("nomeAtivo", posicao.getAtivo().getNome()));
			unidade.add(new CampoProcessamentoJson("empresa", posicao.getAtivo().getEmpresa().getCodigo()));
			unidade.add(new CampoProcessamentoJson("isin", posicao.getAtivo().getIsin()));
			unidade.add(new CampoProcessamentoJson("tipoAcao", posicao.getAtivo().getTipoAcao()));
			unidade.add(new CampoProcessamentoJson("fatorCotacaoAtivo", replaceDotToCommaAndFormat(posicao.getAtivo().getFatorCotacao())));
			unidade.add(new CampoProcessamentoJson("bolsa", posicao.getAtivo().getBolsa().getCodigo()));
			unidade.add(new CampoProcessamentoJson("custoMedioComCorretagem", replaceDotToCommaAndFormat(posicao.getCustoMedioUnitCorr())));
			
			unidade.add(new CampoProcessamentoJson("custodiante", posicao.getQuantidadeSegregadaCustodiante().getCustodiante().getCodigo()));
			unidade.add(new CampoProcessamentoJson("custodianteCentralizador", posicao.getCustodianteCentralizador().getCodigo()));
			unidade.add(new CampoProcessamentoJson("ir",  replaceDotToCommaAndFormat(posicao.getIrSobreRendimento())));
			unidade.add(new CampoProcessamentoJson("estoqueCblc", posicao.getQuantidadeCblcSegregadaCustodiante().getQuantidadeDisponivel().toString()));
			
			return unidade;
		} catch (Exception ex) {
			throw new ConverterException("Ocorreu erro ao converter posição de A Vista de Renda Variavel para Unidade de Processamento Json.", ex);
		}
	}
}