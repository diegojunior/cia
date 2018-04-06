package br.com.totvs.cia.gateway.amplis.processamento.posicao.fundos.investimento.converter;

import static br.com.totvs.cia.infra.util.DateUtil.transformToYYYYMMDD;
import static br.com.totvs.cia.infra.util.NumberUtil.replaceDotToCommaAndFormat;

import br.com.totvs.amplis.api.client.json.PosicaoFundoInvestimentoJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.exception.ConverterException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PosicaoAmplisFundoInvestimentoConverter extends JsonGatewayConverter<PosicaoFundoInvestimentoJson, UnidadeProcessamentoJson> {
	
	private String dataPosicao;
	
	public PosicaoAmplisFundoInvestimentoConverter(final String dataPosicao) {
		this.dataPosicao = dataPosicao;
	}
	
	@Override
	public UnidadeProcessamentoJson convertFrom(final PosicaoFundoInvestimentoJson posicao) {
		try {
			UnidadeProcessamentoJson unidade = new UnidadeProcessamentoJson(ServicoEnum.FUNDO_INVESTIMENTO, this.dataPosicao, "NAO IMPLEMENTADO");
			
			unidade.add(new CampoProcessamentoJson("carteira", "NAO IMPLEMENTADO"));
			unidade.add(new CampoProcessamentoJson("dataPosicao", transformToYYYYMMDD(this.dataPosicao)));
			unidade.add(new CampoProcessamentoJson("codigoFundo", posicao.getFundo().getCodigo()));
			unidade.add(new CampoProcessamentoJson("impostos", replaceDotToCommaAndFormat(posicao.getImpostos())));
			unidade.add(new CampoProcessamentoJson("quantidadeBloqueada", posicao.getQuantidadeBloqueada().toString()));		
			unidade.add(new CampoProcessamentoJson("quantidadeDisponivel", posicao.getQuantidadeDisponivel().toString()));
			unidade.add(new CampoProcessamentoJson("quantidadeTotal", posicao.getQuantidadeTotal().toString()));
			unidade.add(new CampoProcessamentoJson("valorCota", replaceDotToCommaAndFormat(posicao.getValorCota())));
			unidade.add(new CampoProcessamentoJson("valorLiquido", replaceDotToCommaAndFormat(posicao.getValorLiquido())));
			unidade.add(new CampoProcessamentoJson("valorMercado", replaceDotToCommaAndFormat(posicao.getValorMercado())));
			
			return unidade;
		} catch (Exception ex) {
			throw new ConverterException("Ocorreu erro ao converter posição de Fundo de Investimento para Unidade de Processamento Json.", ex);
		}
	}
}