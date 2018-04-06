package br.com.totvs.cia.conciliacao.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.carga.model.CampoUnidadeCarga;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CampoEquivalenteConciliacao {

	@Column(name = "CAMPO_EQUIVALENTE")
	private String campoEquivalente;
	
	@Column(name = "VALOR_EQUIVALENTE")
	private String valorEquivalente;
	
	public CampoEquivalenteConciliacao(final CampoConfiguracaoServico campoEquivalente, final CampoUnidadeCarga campoCargaEquivalente) {
		this.campoEquivalente = campoEquivalente.getLabel();
		this.valorEquivalente = campoCargaEquivalente.getValor();
	}

	public CampoEquivalenteConciliacao(final CampoConfiguracaoServico campoEquivalente) {
		this.campoEquivalente = campoEquivalente.getLabel();
	}

}