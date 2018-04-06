package br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.termo.converter;

import static br.com.totvs.cia.infra.util.DateUtil.transformToYYYYMMDD;
import static br.com.totvs.cia.infra.util.NumberUtil.replaceDotToCommaAndFormat;

import br.com.totvs.amplis.api.client.json.PosicaoTermoJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.exception.ConverterException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PosicaoAmplisRendaVariavelTermoConverter extends JsonGatewayConverter<PosicaoTermoJson, UnidadeProcessamentoJson> {
	
	private String dataPosicao;
	
	public PosicaoAmplisRendaVariavelTermoConverter(final String dataPosicao) {
		this.dataPosicao = dataPosicao;
	}
	
	@Override
	public UnidadeProcessamentoJson convertFrom(final PosicaoTermoJson posicao) {
		try {
			UnidadeProcessamentoJson unidade = new UnidadeProcessamentoJson(ServicoEnum.RENDAVARIAVEL_TERMO, this.dataPosicao, posicao.getCarteira() != null ? posicao.getCarteira().getCodigo() : "");
			
			unidade.add(new CampoProcessamentoJson("carteira", posicao.getCarteira() != null ? posicao.getCarteira().getCodigo() : ""));
			unidade.add(new CampoProcessamentoJson("codigoCblc", posicao.getCarteira() != null ? posicao.getCarteira().getCarteiraBase().getCodigoCblc() : ""));
			unidade.add(new CampoProcessamentoJson("dataPosicao", transformToYYYYMMDD(this.dataPosicao)));
			unidade.add(new CampoProcessamentoJson("tipoMercado", posicao.getTipoMercado()));
			unidade.add(new CampoProcessamentoJson("codigoContrato", posicao.getContrato().getCodigo()));
			unidade.add(new CampoProcessamentoJson("dataVencimentoContrato", transformToYYYYMMDD(posicao.getContrato().getDataVencimento())));
			unidade.add(new CampoProcessamentoJson("tipoContrato", posicao.getContrato().getTipoContrato()));
			unidade.add(new CampoProcessamentoJson("codigoAtivo", posicao.getContrato().getAtivo().getCodigo()));
			unidade.add(new CampoProcessamentoJson("empresa", posicao.getContrato().getAtivo().getEmpresa().getCodigo()));
			unidade.add(new CampoProcessamentoJson("tipoAcao", posicao.getContrato().getAtivo().getTipoAcao()));
			unidade.add(new CampoProcessamentoJson("isin", posicao.getContrato().getAtivo().getIsin()));
			unidade.add(new CampoProcessamentoJson("codigoBolsa", posicao.getContrato().getAtivo().getBolsa().getCodigo()));
			unidade.add(new CampoProcessamentoJson("fatorCotacao", replaceDotToCommaAndFormat(posicao.getContrato().getAtivo().getFatorCotacao())));
			
			unidade.add(new CampoProcessamentoJson("cotacao", replaceDotToCommaAndFormat(posicao.getCotacao())));
			unidade.add(new CampoProcessamentoJson("custoMedioUnitCorr", replaceDotToCommaAndFormat(posicao.getCustoMedioUnitCorr())));
			unidade.add(new CampoProcessamentoJson("qtdDisponivel", posicao.getQtdDisponivel().toString()));
			unidade.add(new CampoProcessamentoJson("qtdTotal", posicao.getQtdTotal().toString()));
			unidade.add(new CampoProcessamentoJson("resultado", replaceDotToCommaAndFormat(posicao.getResultado())));
			unidade.add(new CampoProcessamentoJson("vlBruto", replaceDotToCommaAndFormat(posicao.getVlBruto())));
			unidade.add(new CampoProcessamentoJson("vlCustoTotalCorr", replaceDotToCommaAndFormat(posicao.getVlCustoTotalCorr())));
			unidade.add(new CampoProcessamentoJson("vlLiquido", replaceDotToCommaAndFormat(posicao.getVlLiquido())));
			
			return unidade;
		} catch (Exception ex) {
			throw new ConverterException("Ocorreu erro ao converter posição de Termo de Renda Variavel para Unidade de Processamento Json.", ex);
		}
	}
}