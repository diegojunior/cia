package br.com.totvs.cia.importacao.json;

import java.util.List;

import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.json.Json;
import lombok.Data;

@Data
public class UnidadeImportacaoProcessamentoJson implements Json {

	private static final long serialVersionUID = 1L;

	private String codigoSessao;

	private String idSessao;
	
	private int numeroLinha;
	
	private int numeroLinhaAnterior;

	private List<CampoImportacaoProcessamentoJson> campos;

	public UnidadeImportacaoProcessamentoJson() {
		this.campos = Lists.newArrayList();
	}

	public void add(final CampoImportacaoProcessamentoJson campo) {
		this.campos.add(campo);
	}
}