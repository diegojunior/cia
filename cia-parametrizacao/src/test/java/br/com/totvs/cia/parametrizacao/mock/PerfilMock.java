package br.com.totvs.cia.parametrizacao.mock;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;

public class PerfilMock {

	public static PerfilConciliacao mock(String id, String codigo, Layout layout) {
		PerfilConciliacao perfilConciliacao = new PerfilConciliacao();
		//ConfiguracaoPerfilConciliacao sessaoConciliacao = SessaoConciliacaoMock.mock();
		perfilConciliacao.setId(id);
		perfilConciliacao.setCodigo(codigo);
		perfilConciliacao.setLayout(layout);
		perfilConciliacao.setSistema(SistemaEnum.AMPLIS);
//		perfilConciliacao.setStatusPerfil(StatusPerfilEnum.ATIVO);
//		perfilConciliacao.getSessoesConciliacao().add(sessaoConciliacao);
		
		return perfilConciliacao;
	}
	
	public static PerfilConciliacao mock2Chaves(String id, String codigo, Layout layout) {
		PerfilConciliacao perfilConciliacao = new PerfilConciliacao();
		//ConfiguracaoPerfilConciliacao sessaoConciliacao = SessaoConciliacaoMock.mock2Chaves();
		perfilConciliacao.setId(id);
		perfilConciliacao.setCodigo(codigo);
		perfilConciliacao.setLayout(layout);
		perfilConciliacao.setSistema(SistemaEnum.AMPLIS);
//		perfilConciliacao.setStatusPerfil(StatusPerfilEnum.ATIVO);
//		perfilConciliacao.getSessoesConciliacao().add(sessaoConciliacao);
		
		return perfilConciliacao;
	}
	
}
