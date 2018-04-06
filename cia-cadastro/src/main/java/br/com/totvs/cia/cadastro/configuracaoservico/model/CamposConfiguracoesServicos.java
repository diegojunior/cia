package br.com.totvs.cia.cadastro.configuracaoservico.model;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

public class CamposConfiguracoesServicos implements Iterable<CampoConfiguracaoServico> {
	
	private List<CampoConfiguracaoServico> campos;
	
	public CamposConfiguracoesServicos(final List<CampoConfiguracaoServico> campos) {
		this.campos = campos;
	}
	
	public CamposConfiguracoesServicos(final ConfiguracaoServico configuracaoServico, final List<CampoConfiguracaoServico> campos) {
		this.campos = Lists.newArrayList();
		for (CampoConfiguracaoServico campo : campos) {
			this.campos.add(new CampoConfiguracaoServico(campo, configuracaoServico));
		} 
	}
	
	public static List<CampoConfiguracaoServico> build(ConfiguracaoServico configuracaoServico, List<CampoConfiguracaoServico> campos) {
		return Lists.newArrayList(new CamposConfiguracoesServicos(configuracaoServico, campos));
	}
	
	@Override
	public Iterator<CampoConfiguracaoServico> iterator() {
		return this.campos.iterator();
	}
	
	public Integer size() {
		return this.campos.size();
	}
}