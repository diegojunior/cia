package br.com.totvs.cia.carga.model;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import lombok.Getter;

public class CamposUnidadeCarga implements Iterable<CampoUnidadeCarga> {
	
	@Getter
	private List<CampoUnidadeCarga> campos;
	
	public CamposUnidadeCarga(final UnidadeCarga unidadeCarga, final ConfiguracaoServico servico,
			final List<CampoProcessamentoJson> campos) {
		this.campos = Lists.newArrayList();
		for (CampoProcessamentoJson campo : campos) {
			CampoConfiguracaoServico campoServico = servico.getBy(campo.getCampo());
			if (campoServico != null) {
				this.campos.add(new CampoUnidadeCarga(campoServico, unidadeCarga, campo.getValor()));
			}
		}
	}
	
	public static List<CampoUnidadeCarga> build(final UnidadeCarga unidadeCarga, final ConfiguracaoServico servico,
			final List<CampoProcessamentoJson> campos) {
		return new CamposUnidadeCarga (unidadeCarga, servico, campos).getCampos();
	}
	
	@Override
	public Iterator<CampoUnidadeCarga> iterator() {
		return this.campos.iterator();
	}
	
	public CampoUnidadeCarga get(final Integer index) {
		return this.campos.get(index);
	}	
}