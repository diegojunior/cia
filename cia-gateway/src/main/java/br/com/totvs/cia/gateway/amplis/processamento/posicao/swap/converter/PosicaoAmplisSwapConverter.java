package br.com.totvs.cia.gateway.amplis.processamento.posicao.swap.converter;

import static br.com.totvs.cia.infra.util.DateUtil.transformToYYYYMMDD;
import static br.com.totvs.cia.infra.util.NumberUtil.replaceDotToCommaAndFormat;

import br.com.totvs.amplis.api.client.json.SwapJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.exception.ConverterException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PosicaoAmplisSwapConverter extends JsonGatewayConverter<SwapJson, UnidadeProcessamentoJson> {
	
	private String carteira;
	
	private String dataPosicao;
	
	public PosicaoAmplisSwapConverter(final String carteira, final String dataPosicao) {
		this.carteira = carteira;
		this.dataPosicao = dataPosicao;
	}
	
	@Override
	public UnidadeProcessamentoJson convertFrom(final SwapJson posicao) {
		try {
			UnidadeProcessamentoJson unidade = new UnidadeProcessamentoJson(ServicoEnum.SWAP, this.dataPosicao, this.carteira);
			
			unidade.add(new CampoProcessamentoJson("carteira", this.carteira));
			unidade.add(new CampoProcessamentoJson("dataPosicao", transformToYYYYMMDD(this.dataPosicao)));
			unidade.add(new CampoProcessamentoJson("codigoOperacao", posicao.getCodigoOperacao()));
			unidade.add(new CampoProcessamentoJson("contraparte", posicao.getContraparte()));
			unidade.add(new CampoProcessamentoJson("corretora", posicao.getCorretora()));
			unidade.add(new CampoProcessamentoJson("indexadorAtivo", posicao.getIndexadorAtivo()));
			unidade.add(new CampoProcessamentoJson("indexadorPassivo", posicao.getIndexadorPassivo()));		
			unidade.add(new CampoProcessamentoJson("financeiro", replaceDotToCommaAndFormat(posicao.getFinanceiro())));
			unidade.add(new CampoProcessamentoJson("financeiroApropriadoBruto", replaceDotToCommaAndFormat(posicao.getFinanceiroApropriadoBruto())));
			unidade.add(new CampoProcessamentoJson("financeiroApropriadoLiquido", replaceDotToCommaAndFormat(posicao.getFinanceiroApropriadoLiquido())));
			unidade.add(new CampoProcessamentoJson("financeiroAtivo", replaceDotToCommaAndFormat(posicao.getFinanceiroAtivo())));
			unidade.add(new CampoProcessamentoJson("financeiroPassivo", replaceDotToCommaAndFormat(posicao.getFinanceiroPassivo())));
			
			return unidade;
		} catch (Exception ex) {
			throw new ConverterException("Ocorreu erro ao converter posição de Swap para Unidade de Processamento Json.", ex);
		}
	}
}