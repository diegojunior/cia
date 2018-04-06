package br.com.totvs.cia.gateway.amplis.processamento.posicao.derivativos.futuros.converter;

import static br.com.totvs.cia.infra.util.DateUtil.transformToYYYYMMDD;
import static br.com.totvs.cia.infra.util.NumberUtil.replaceDotToCommaAndFormat;

import br.com.totvs.amplis.api.client.json.PosicaoFuturoJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.exception.ConverterException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PosicaoAmplisDerivativosFuturoConverter extends JsonGatewayConverter<PosicaoFuturoJson, UnidadeProcessamentoJson> {
	
	private String dataPosicao;
	
	public PosicaoAmplisDerivativosFuturoConverter(final String dataPosicao) {
		this.dataPosicao = dataPosicao;
	}
	
	@Override
	public UnidadeProcessamentoJson convertFrom(final PosicaoFuturoJson posicao) {
		try {
			UnidadeProcessamentoJson unidade = new UnidadeProcessamentoJson(ServicoEnum.DERIVATIVOS_FUTUROS, this.dataPosicao, "NAO IMPLEMENTADO");
	
			unidade.add(new CampoProcessamentoJson("carteira", "NAO IMPLEMENTADO"));
			unidade.add(new CampoProcessamentoJson("dataPosicao", transformToYYYYMMDD(this.dataPosicao)));
			unidade.add(new CampoProcessamentoJson("corretora", posicao.getCorretora().getCodigo()));
			unidade.add(new CampoProcessamentoJson("vencimento", posicao.getVencimentoFuturo().getCodigo()));
			unidade.add(new CampoProcessamentoJson("codigoAtivo", posicao.getVencimentoFuturo().getAtivoFuturo().getCodigo()));
			unidade.add(new CampoProcessamentoJson("codigoAtivoNaBolsa", posicao.getVencimentoFuturo().getAtivoFuturo().getCodigoAtivoNaBolsa()));
			unidade.add(new CampoProcessamentoJson("quantidade", posicao.getQuantidade().toString()));
			unidade.add(new CampoProcessamentoJson("valorMercado", replaceDotToCommaAndFormat(posicao.getValorMercado())));
			
			return unidade;
		} catch (Exception ex) {
			throw new ConverterException("Ocorreu erro ao converter posição de Futuros de Derivativos para Unidade de Processamento Json.", ex);
		}
	}
}