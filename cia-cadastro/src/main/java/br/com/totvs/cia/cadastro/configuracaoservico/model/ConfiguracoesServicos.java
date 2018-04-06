package br.com.totvs.cia.cadastro.configuracaoservico.model;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Iterators;

public class ConfiguracoesServicos implements Iterable<ConfiguracaoServico> {
	
	private List<ConfiguracaoServico> servicos;
	
	public ConfiguracoesServicos(final List<ConfiguracaoServico> servicos) {
		this.servicos = servicos;
	}
	
	@Override
	public Iterator<ConfiguracaoServico> iterator() {
		return this.servicos.iterator();
	}

	public Boolean isLast(ConfiguracaoServico servicoParam) {
		return Iterators.getLast(this.servicos.iterator()).equals(servicoParam);
	}
	
	public Integer size() {
		return this.servicos.size();
	}

	public List<ConfiguracaoServico> add(final List<ConfiguracaoServico> servicos) {
		
		for (ConfiguracaoServico servico : this.servicos) {
			//servico.setLoad(false);
		}
		
		for (ConfiguracaoServico servico : servicos) {
			if(this.servicos.contains(servico)){
				//this.servicos.get(this.servicos.indexOf(servico)).setLoad(true);
			}else {
				//servico.setLoad(true);
				this.servicos.add(servico);
			}
		}
		
		return this.servicos;
	}
}