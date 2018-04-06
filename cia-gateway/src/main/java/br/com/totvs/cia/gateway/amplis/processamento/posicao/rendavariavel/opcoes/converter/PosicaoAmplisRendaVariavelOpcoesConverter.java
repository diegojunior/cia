package br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.opcoes.converter;

import static br.com.totvs.cia.infra.util.DateUtil.transformToYYYYMMDD;
import static br.com.totvs.cia.infra.util.NumberUtil.replaceDotToCommaAndFormat;

import br.com.totvs.amplis.api.client.json.PosicaoOpcaoRVJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.exception.ConverterException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PosicaoAmplisRendaVariavelOpcoesConverter extends JsonGatewayConverter<PosicaoOpcaoRVJson, UnidadeProcessamentoJson> {
	
	private String dataPosicao;
	
	public PosicaoAmplisRendaVariavelOpcoesConverter(final String dataPosicao) {
		this.dataPosicao = dataPosicao;
	}
	
	@Override
	public UnidadeProcessamentoJson convertFrom(final PosicaoOpcaoRVJson posicao) {
		try {
			UnidadeProcessamentoJson unidade = new UnidadeProcessamentoJson(ServicoEnum.RENDAVARIAVEL_OPCOES, this.dataPosicao, posicao.getCarteira() != null ? posicao.getCarteira().getCodigo() : "");
			
			unidade.add(new CampoProcessamentoJson("carteira", posicao.getCarteira() != null ? posicao.getCarteira().getCodigo() : ""));
			unidade.add(new CampoProcessamentoJson("codigoCblc", posicao.getCarteira() != null ? posicao.getCarteira().getCarteiraBase().getCodigoCblc() : ""));
			unidade.add(new CampoProcessamentoJson("dataPosicao", transformToYYYYMMDD(this.dataPosicao)));
			unidade.add(new CampoProcessamentoJson("tipoMercado", posicao.getTipoMercado()));
			unidade.add(new CampoProcessamentoJson("cotacao", replaceDotToCommaAndFormat(posicao.getCotacao())));
			unidade.add(new CampoProcessamentoJson("custoMedioUnitCorr", replaceDotToCommaAndFormat(posicao.getCustoMedioUnitCorr())));
			unidade.add(new CampoProcessamentoJson("qtdDisponivel", posicao.getQtdDisponivel().toString()));
			unidade.add(new CampoProcessamentoJson("qtdTotal", posicao.getQtdTotal().toString()));
			unidade.add(new CampoProcessamentoJson("resultado", replaceDotToCommaAndFormat(posicao.getResultado())));
			unidade.add(new CampoProcessamentoJson("vlBruto", replaceDotToCommaAndFormat(posicao.getVlBruto())));
			unidade.add(new CampoProcessamentoJson("vlCustoTotalCorr", replaceDotToCommaAndFormat(posicao.getVlCustoTotalCorr())));
			unidade.add(new CampoProcessamentoJson("vlLiquido", replaceDotToCommaAndFormat(posicao.getVlLiquido())));
			
			unidade.add(new CampoProcessamentoJson("codigoAtivo", posicao.getAtivo().getCodigo()));
			unidade.add(new CampoProcessamentoJson("nomeAtivo", posicao.getAtivo().getNome()));
			unidade.add(new CampoProcessamentoJson("empresa", posicao.getAtivo().getEmpresa()));		
			unidade.add(new CampoProcessamentoJson("tipoAcao", posicao.getAtivo().getTipoAcao()));
			
			return unidade;
		} catch (Exception ex) {
			throw new ConverterException("Ocorreu erro ao converter posição de Opções de Renda Variavel para Unidade de Processamento Json.", ex);
		}
	}
}