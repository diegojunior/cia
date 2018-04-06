package br.com.totvs.cia.parametrizacao.mock;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.TipoServicoEnum;

public class ConfiguracaoServicoMock {

	public static ConfiguracaoServico mockConfiguracaoServico() {
		ConfiguracaoServico configuracaoServico = new ConfiguracaoServico();
		configuracaoServico.setId("1");
		configuracaoServico.setCodigo("RVAVISTA");
		configuracaoServico.setDescricao("teste");
		configuracaoServico.setServico(ServicoEnum.RENDAVARIAVEL_AVISTA);
		configuracaoServico.setSistema(SistemaEnum.AMPLIS);
		configuracaoServico.setTipoServico(TipoServicoEnum.POSICAO);
		//configuracaoServico.setUrl("http://localhost:8081/amplisapi/ws/v1/rendavariavel/posicoes/{carteira}/{dataPosicao}/{tipoPosicao}");
		return configuracaoServico;
	}

}