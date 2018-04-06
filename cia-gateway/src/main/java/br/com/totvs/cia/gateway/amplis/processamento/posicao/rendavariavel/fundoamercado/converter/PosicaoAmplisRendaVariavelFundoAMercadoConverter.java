package br.com.totvs.cia.gateway.amplis.processamento.posicao.rendavariavel.fundoamercado.converter;

import static br.com.totvs.cia.infra.util.DateUtil.transformToYYYYMMDD;
import static br.com.totvs.cia.infra.util.NumberUtil.replaceDotToCommaAndFormat;

import br.com.totvs.amplis.api.client.json.PosicaoFundoAMercadoJson;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.gateway.core.infra.converter.JsonGatewayConverter;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import br.com.totvs.cia.gateway.core.processamento.json.UnidadeProcessamentoJson;
import br.com.totvs.cia.infra.exception.ConverterException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PosicaoAmplisRendaVariavelFundoAMercadoConverter extends JsonGatewayConverter<PosicaoFundoAMercadoJson, UnidadeProcessamentoJson> {
	
	private String dataPosicao;
	
	public PosicaoAmplisRendaVariavelFundoAMercadoConverter(final String dataPosicao) {
		this.dataPosicao = dataPosicao;
	}
	
	@Override
	public UnidadeProcessamentoJson convertFrom(final PosicaoFundoAMercadoJson posicao) {
		try {
			UnidadeProcessamentoJson unidade = new UnidadeProcessamentoJson(ServicoEnum.RENDAVARIAVEL_FUNDO_A_MERCADO, this.dataPosicao, posicao.getCarteira().getCodigo());
			
			String bolsa = posicao.getAtivo().getBolsa() != null ? posicao.getAtivo().getBolsa().getCodigo() : null;
			String empresa = posicao.getAtivo().getEmpresa() != null ? posicao.getAtivo().getEmpresa().getCodigo() : null;
			String administrador = posicao.getAtivo().getAdministrador() != null ? posicao.getAtivo().getAdministrador().getCodigo() : null;
			String custodianteCentralizador = posicao.getCarteira().getCustodianteCentralizador() != null ? posicao.getCarteira().getCustodianteCentralizador().getCodigo() : null;
			
			unidade.add(new CampoProcessamentoJson("carteira", posicao.getCarteira().getCodigo()));
			unidade.add(new CampoProcessamentoJson("codigoCblc", posicao.getCarteira().getCarteiraBase().getCodigoCblc()));
			unidade.add(new CampoProcessamentoJson("custodianteCentralizador", custodianteCentralizador));
			unidade.add(new CampoProcessamentoJson("custodiante", posicao.getCustodiante().getCodigo()));
			unidade.add(new CampoProcessamentoJson("codigoAtivo", posicao.getAtivo().getCodigo()));
			unidade.add(new CampoProcessamentoJson("nomeAtivo", posicao.getAtivo().getNome()));
			unidade.add(new CampoProcessamentoJson("isin", posicao.getAtivo().getIsin()));
			unidade.add(new CampoProcessamentoJson("tipoAcao", posicao.getAtivo().getTipoAcao()));
			unidade.add(new CampoProcessamentoJson("administrador", administrador));
			unidade.add(new CampoProcessamentoJson("empresa", empresa));
			unidade.add(new CampoProcessamentoJson("bolsa",  bolsa));
			unidade.add(new CampoProcessamentoJson("fatorCotacao", replaceDotToCommaAndFormat(posicao.getAtivo().getFatorCotacao())));
			unidade.add(new CampoProcessamentoJson("cotacao", replaceDotToCommaAndFormat(posicao.getCotacao())));
			unidade.add(new CampoProcessamentoJson("tipoMercado", posicao.getTipoMercado()));
			unidade.add(new CampoProcessamentoJson("tipoFundo", posicao.getTipoFundo()));
			unidade.add(new CampoProcessamentoJson("dataPosicao", transformToYYYYMMDD(this.dataPosicao)));
			unidade.add(new CampoProcessamentoJson("qtdDisponivel", posicao.getQuantidadeDisponivel().toString()));
			unidade.add(new CampoProcessamentoJson("qtdTotal", posicao.getQuantidadeTotal().toString()));
			unidade.add(new CampoProcessamentoJson("qtdBloqueada", posicao.getQuantidadeBloqueada().toString()));
			unidade.add(new CampoProcessamentoJson("vlLiquido", replaceDotToCommaAndFormat((posicao.getValorLiquido()))));
			unidade.add(new CampoProcessamentoJson("vlMercado", replaceDotToCommaAndFormat((posicao.getValorMercado()))));
			unidade.add(new CampoProcessamentoJson("ir", replaceDotToCommaAndFormat((posicao.getIr()))));
			
			return unidade;
		} catch (Exception ex) {
			throw new ConverterException("Ocorreu erro ao converter posição de Fundo A Mercado de Renda Variavel para Unidade de Processamento Json.", ex);
		}
	}
}