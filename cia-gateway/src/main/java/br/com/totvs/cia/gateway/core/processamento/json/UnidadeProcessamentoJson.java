package br.com.totvs.cia.gateway.core.processamento.json;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.infra.json.Json;
import lombok.Getter;

@Getter
public class UnidadeProcessamentoJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	private String dataPosicao;
	
	private ServicoEnum servico;
	
	private String cliente;
	
	private List<CampoProcessamentoJson> campos;
	
	public UnidadeProcessamentoJson (final ServicoEnum servico, final String dataPosicao, final String carteira) {
		this.dataPosicao = dataPosicao;
		this.servico = servico;
		this.cliente = carteira;
		this.campos = Lists.newArrayList();
	}
	
	public UnidadeProcessamentoJson() {
		this.campos = Lists.newArrayList();
	}
	
	public void add(final CampoProcessamentoJson campo){
		this.campos.add(campo);
	}
}