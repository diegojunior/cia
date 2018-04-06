package br.com.totvs.cia.carga.model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.gateway.core.processamento.json.CampoProcessamentoJson;
import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.infra.util.Constants;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.CampoPerfilConciliacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@AllArgsConstructor
@Table(name = "CA_UNIDADE_CARGA")
@ToString(callSuper = false, of = { "id" })
public class UnidadeCarga implements Model, Comparable<UnidadeCarga> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-unidadecarga-uuid")
	@GenericGenerator(name = "system-unidadecarga-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name = "ID_LOTE_CLIENTE", nullable = false)
	private LoteCliente loteCliente;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "unidade", orphanRemoval = true)
	private List<CampoUnidadeCarga> campos;

	public UnidadeCarga() {
		this.campos = Lists.newArrayList();
	}
	
	public UnidadeCarga(final LoteCliente loteCliente, final List<CampoProcessamentoJson> campos) {
		this.loteCliente = loteCliente;
		this.campos = CamposUnidadeCarga.build(this, loteCliente.getServico(), campos);
	}

	public void add(final CampoUnidadeCarga campo) {
		this.campos.add(campo);
	}

	public Boolean isPossuiCampos() {
		return !this.campos.isEmpty();
	}

	public CampoUnidadeCarga getCampoPor(final CampoConfiguracaoServico campoConfiguracao) {
		return this.getCampos()
				.stream()
				.filter(campo -> 
					campo.getCampoServico() != null && campo.getCampoServico().equals(campoConfiguracao))
				.findAny()
				.get();
	}
	
	public CampoUnidadeCarga getCampoPor(final String campoConfiguracao) {
		return this.getCampos()
				.stream()
				.filter(campo -> 
				campo.getCampoServico() != null && campo.getCampoServico().getCampo().equals(campoConfiguracao))
				.findAny()
				.get();
	}

	public Integer getHashChave(final List<CampoPerfilConciliacao> camposChave) {
		int result = 1;
		for (final CampoPerfilConciliacao chave : camposChave) {
			final CampoUnidadeCarga campoCarga = this.getCampoPor(chave.getCampoCarga());
			final String valorCarga = campoCarga != null && campoCarga.getValor() != null ? campoCarga.getValor() : "";
			result = result + valorCarga.hashCode();
		}
		return result;
	}

	@Override
	public int compareTo(final UnidadeCarga outraUnidade) {
		int comparador = 0;
		for (CampoUnidadeCarga campo : this.campos) {
			for (CampoUnidadeCarga outroCampo : outraUnidade.getCampos()) {
				if (campo.getCampoServico().equals(outroCampo.getCampoServico())) {
					String valor = campo.getValor() != null ? campo.getValor() : "";
					String outroValor = outroCampo.getValor() != null ? outroCampo.getValor() : "";
					comparador = valor.compareTo(outroValor);
					break;
				}
			}
			if (comparador != 0) {
				return comparador;
			}
		}
		return comparador;
	}

	public Integer getHashChave(final Map<String, CampoPerfilConciliacao> chaves) {
		int result = 1;
		for (final String campo : chaves.keySet()) {
			final CampoUnidadeCarga chaveImportacao = this.getCampoPor(campo);
			result = result + chaveImportacao.getValor().hashCode();
			
		}
		return result;
	}

	public void consolidar(final Map<String, CampoPerfilConciliacao> camposConciliaveis,
			final Map<String, CampoPerfilConciliacao> camposInformativos, 
			final UnidadeCarga unidadeConsolidar) {
		for (CampoUnidadeCarga campo : this.campos) {
			boolean campoConciliavel = camposConciliaveis.containsKey(campo.getCampoServico().getCampo());
			if (campoConciliavel) {
				this.atualizaValorConsolidado(unidadeConsolidar, campo);
			} else {
				boolean campoInformativo = camposInformativos.containsKey(campo.getCampoServico().getCampo());
				if (campoInformativo) {
					this.atualizaValorConsolidado(unidadeConsolidar, campo);
				}
			}
		}
	}
	
	private void atualizaValorConsolidado(final UnidadeCarga unidadeConsolidar,
			final CampoUnidadeCarga campo) {
		CampoUnidadeCarga campoAConsolidar = unidadeConsolidar.getCampoPor(campo.getCampoServico());
		if (campo.isConsolidated()) {
			BigDecimal valor1 = new BigDecimal(campo.getValor());
			BigDecimal valor2 = new BigDecimal(campoAConsolidar.getValor());
			campo.atualizaValorConsolidado(valor1.add(valor2).toPlainString());
		} else {
			if (!campo.getValor().equals(campoAConsolidar.getValor())) {
				campo.atualizaValorConsolidado(Constants.NAO_CONSOLIDADO);
			}
		}
	}
}