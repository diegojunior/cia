package br.com.totvs.cia.conciliacao.model;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.carga.model.CampoUnidadeCarga;
import br.com.totvs.cia.importacao.model.CampoImportacao;
import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CO_CAMPO_CHAVE_CONCILIACAO")
@ToString(callSuper = false, of = {"id"})
public class CampoChaveConciliacao implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-campochaveconciliacao-uuid")
	@GenericGenerator(name = "system-campochaveconciliacao-uuid", strategy = "uuid")
	private String id;

	@Column(name = "CAMPO")
	private String campo;
	
	@Column(name = "VALOR")
	private String valor;
	
	@Embedded
	private CampoEquivalenteConciliacao campoEquivalenteConciliacao;
	
	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_CONCILIACAO", nullable = false)
	private UnidadeConciliacao unidade;
	
	private CampoChaveConciliacao(final String codigo, final String valor, final UnidadeConciliacao unidade) {
		this.campo = codigo;
		this.valor = valor;
		this.unidade = unidade;
		this.unidade.getCamposChave().add(this);
	}
	
	public static CampoChaveConciliacao buildCampoChaveConciliacao(final String label, final CampoUnidadeCarga campoCarga,
			final UnidadeConciliacao unidadeConciliacao) {
		return new CampoChaveConciliacao(label, campoCarga.getValor(), unidadeConciliacao);
	}
	
	public static CampoChaveConciliacao buildCampoChaveConciliacao(final CampoImportacao campoImportacao,
			final UnidadeConciliacao unidadeConciliacao) {
		return new CampoChaveConciliacao(campoImportacao.getCampo().getCodigo(), campoImportacao.getValor(), unidadeConciliacao);
	}
	
	public CampoChaveConciliacao comCampoEquivalente(final CampoConfiguracaoServico campoEquivalente, final CampoUnidadeCarga campoCargaEquivalente) {
		if (campoEquivalente != null) {
			this.campoEquivalenteConciliacao = new CampoEquivalenteConciliacao(campoEquivalente, campoCargaEquivalente);
			
		}
		return this;
		
	}
	
	public void comCampoEquivalenteVazio(final CampoConfiguracaoServico campoEquivalente) {
		this.campoEquivalenteConciliacao = new CampoEquivalenteConciliacao(campoEquivalente);		
	}

	public String getValor() {
		return this.valor;
	}
}