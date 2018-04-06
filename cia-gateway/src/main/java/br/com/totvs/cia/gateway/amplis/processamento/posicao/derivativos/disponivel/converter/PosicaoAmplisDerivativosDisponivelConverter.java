package br.com.totvs.cia.gateway.amplis.processamento.posicao.derivativos.disponivel.converter;

import static br.com.totvs.cia.infra.util.DateUtil.transformToYYYYMMDD;
import static br.com.totvs.cia.infra.util.NumberUtil.replaceDotToCommaAndFormat;

import br.com.totvs.amplis.api.client.json.PosicaoDisponivelJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.exception.ConverterException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PosicaoAmplisDerivativosDisponivelConverter extends JsonGatewayConverter<PosicaoDisponivelJson, UnidadeProcessamentoJson> {
	
	private String dataPosicao;
	
	public PosicaoAmplisDerivativosDisponivelConverter(final String dataPosicao) {
		this.dataPosicao = dataPosicao;
	}
	
	@Override
	public UnidadeProcessamentoJson convertFrom(final PosicaoDisponivelJson posicao) {
		try {
			UnidadeProcessamentoJson unidade = new UnidadeProcessamentoJson(ServicoEnum.DERIVATIVOS_DISPONIVEL, this.dataPosicao, "NAO IMPLEMENTADO");
			
			unidade.add(new CampoProcessamentoJson("carteira", "NAO IMPLEMENTADO"));
			unidade.add(new CampoProcessamentoJson("dataPosicao", transformToYYYYMMDD(this.dataPosicao)));
			unidade.add(new CampoProcessamentoJson("custodiante", posicao.getCustodiante()));
			unidade.add(new CampoProcessamentoJson("codigoAtivo", posicao.getAtivoDisponivel().getCodigo()));
			unidade.add(new CampoProcessamentoJson("codigoAtivoNaBolsa", posicao.getAtivoDisponivel().getCodigoAtivoNaBolsa()));
			unidade.add(new CampoProcessamentoJson("cotacaoMercado", replaceDotToCommaAndFormat(posicao.getCotacaoMercado())));
			unidade.add(new CampoProcessamentoJson("custoMedioComCorretagem", replaceDotToCommaAndFormat(posicao.getCustoMedioComCorretagem())));
			unidade.add(new CampoProcessamentoJson("custoMedioSemCorretagem", replaceDotToCommaAndFormat(posicao.getCustoMedioSemCorretagem())));
			unidade.add(new CampoProcessamentoJson("custoTotalComCorretagem", replaceDotToCommaAndFormat(posicao.getCustoTotalComCorretagem())));		
			unidade.add(new CampoProcessamentoJson("custoTotalSemCorretagem", replaceDotToCommaAndFormat(posicao.getCustoTotalSemCorretagem())));
			unidade.add(new CampoProcessamentoJson("qtdBloqueada", posicao.getQtdBloqueada().toString()));
			unidade.add(new CampoProcessamentoJson("qtdDisponivel", posicao.getQtdDisponivel().toString()));
			unidade.add(new CampoProcessamentoJson("qtdTotal", posicao.getQtdTotal().toString()));
			unidade.add(new CampoProcessamentoJson("resultado", replaceDotToCommaAndFormat(posicao.getResultado())));
			unidade.add(new CampoProcessamentoJson("valorMercado", replaceDotToCommaAndFormat(posicao.getValorMercado())));
			
			return unidade;
		} catch (Exception ex) {
			throw new ConverterException("Ocorreu erro ao converter posição de Disponivel de Derivativos para Unidade de Processamento Json.", ex);
		}
	}
}