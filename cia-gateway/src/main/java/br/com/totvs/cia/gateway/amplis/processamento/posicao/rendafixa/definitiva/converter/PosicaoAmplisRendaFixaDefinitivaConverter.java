package br.com.totvs.cia.gateway.amplis.processamento.posicao.rendafixa.definitiva.converter;

import static br.com.totvs.cia.infra.util.DateUtil.transformToYYYYMMDD;
import static br.com.totvs.cia.infra.util.NumberUtil.replaceDotToCommaAndFormat;

import br.com.totvs.amplis.api.client.json.PosicaoDefinitivaRFJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.exception.ConverterException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PosicaoAmplisRendaFixaDefinitivaConverter extends JsonGatewayConverter<PosicaoDefinitivaRFJson, UnidadeProcessamentoJson> {
	
	private String dataPosicao;
	
	public PosicaoAmplisRendaFixaDefinitivaConverter(final String dataPosicao) {
		this.dataPosicao = dataPosicao;
	}
	
	@Override
	public UnidadeProcessamentoJson convertFrom(final PosicaoDefinitivaRFJson posicao) {
		try {
			final UnidadeProcessamentoJson unidade = new UnidadeProcessamentoJson(ServicoEnum.RENDAFIXA_DEFINITIVA, this.dataPosicao, posicao.getCarteira().getCodigo());
			
			unidade.add(new CampoProcessamentoJson("carteira", posicao.getCarteira().getCodigo()));
			unidade.add(new CampoProcessamentoJson("data", transformToYYYYMMDD(posicao.getData())));
			unidade.add(new CampoProcessamentoJson("dataEmissao", transformToYYYYMMDD(posicao.getDataEmissao())));
			unidade.add(new CampoProcessamentoJson("dataMovimento", transformToYYYYMMDD(posicao.getDataMovimento())));
			unidade.add(new CampoProcessamentoJson("dataVencimento", transformToYYYYMMDD(posicao.getDataVencimento())));
			unidade.add(new CampoProcessamentoJson("operacao", posicao.getOperacao()));
			unidade.add(new CampoProcessamentoJson("serie", posicao.getSerie()));
			unidade.add(new CampoProcessamentoJson("direitoObrigacao", posicao.getDireitoObrigacao().toString()));
			unidade.add(new CampoProcessamentoJson("codigoEmissao", posicao.getEmissao().getCodigo()));
			//unidade.add(new CampoProcessamentoJson("codigoEmissor", posicao.getEmissor().getCodigo()));
			unidade.add(new CampoProcessamentoJson("imposto", replaceDotToCommaAndFormat(posicao.getImposto())));
			unidade.add(new CampoProcessamentoJson("percentualSerie", replaceDotToCommaAndFormat(posicao.getPercentualSerie())));
			unidade.add(new CampoProcessamentoJson("pu", replaceDotToCommaAndFormat(posicao.getPu())));
			unidade.add(new CampoProcessamentoJson("quantidade", posicao.getQuantidade().toString()));
			unidade.add(new CampoProcessamentoJson("resultado", replaceDotToCommaAndFormat(posicao.getResultado())));
			unidade.add(new CampoProcessamentoJson("codigoTitulo", posicao.getTitulo().getCodigo().toString()));
			unidade.add(new CampoProcessamentoJson("valorBruto", replaceDotToCommaAndFormat(posicao.getValorBruto())));
			unidade.add(new CampoProcessamentoJson("valorCompra", replaceDotToCommaAndFormat(posicao.getValorCompra())));
			unidade.add(new CampoProcessamentoJson("valorLiquido", replaceDotToCommaAndFormat(posicao.getValorLiquido())));
			unidade.add(new CampoProcessamentoJson("valorReducao", replaceDotToCommaAndFormat(posicao.getValorReducao())));
			
			return unidade;
		} catch (final Exception ex) {
			throw new ConverterException("Ocorreu erro ao converter posição de Definitiva de Renda Fixa para Unidade de Processamento Json.", ex);
		}
	}
}