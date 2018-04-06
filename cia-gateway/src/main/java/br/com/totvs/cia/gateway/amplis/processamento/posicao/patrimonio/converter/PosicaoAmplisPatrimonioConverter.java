package br.com.totvs.cia.gateway.amplis.processamento.posicao.patrimonio.converter;

import static br.com.totvs.cia.infra.util.DateUtil.transformToYYYYMMDD;
import static br.com.totvs.cia.infra.util.NumberUtil.replaceDotToCommaAndFormat;

import br.com.totvs.amplis.api.client.json.PosicaoPatrimonioJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.exception.ConverterException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PosicaoAmplisPatrimonioConverter extends JsonGatewayConverter<PosicaoPatrimonioJson, UnidadeProcessamentoJson> {
	
	private String dataPosicao;
	
	public PosicaoAmplisPatrimonioConverter(final String dataPosicao) {
		this.dataPosicao = dataPosicao;
	}
	
	@Override
	public UnidadeProcessamentoJson convertFrom(final PosicaoPatrimonioJson posicao) {
		try {
			UnidadeProcessamentoJson unidade = new UnidadeProcessamentoJson(ServicoEnum.PATRIMONIO, this.dataPosicao, posicao.getCarteira());
			
			unidade.add(new CampoProcessamentoJson("carteira", posicao.getCarteira()));
			unidade.add(new CampoProcessamentoJson("data", transformToYYYYMMDD(posicao.getData())));
			unidade.add(new CampoProcessamentoJson("tipoPosicao", posicao.getTipoPosicao()));
			unidade.add(new CampoProcessamentoJson("saldoCaixa", replaceDotToCommaAndFormat(posicao.getPosicaoCaixa().getSaldo())));
			unidade.add(new CampoProcessamentoJson("saldoCPR", replaceDotToCommaAndFormat(posicao.getPosicaoCPR().getSaldo())));
			unidade.add(new CampoProcessamentoJson("valorCotaLiquida", replaceDotToCommaAndFormat(posicao.getValorCotaLiquida())));
			unidade.add(new CampoProcessamentoJson("valorPatrimonioLiquido", replaceDotToCommaAndFormat(posicao.getValorPatrimonioLiquido())));
			
			return unidade;
		} catch (Exception ex) {
			throw new ConverterException("Ocorreu erro ao converter posição de Patrimonio para Unidade de Processamento Json.", ex);
		}
	}
}