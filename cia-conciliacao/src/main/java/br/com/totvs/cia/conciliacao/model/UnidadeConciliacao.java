package br.com.totvs.cia.conciliacao.model;

import static br.com.totvs.cia.conciliacao.model.CampoChaveConciliacao.buildCampoChaveConciliacao;
import static br.com.totvs.cia.conciliacao.model.CampoConciliacao.buildCampoConciliacao;
import static br.com.totvs.cia.conciliacao.model.CampoInformativo.buildCampo;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.Lists;

import br.com.totvs.cia.carga.model.CampoUnidadeCarga;
import br.com.totvs.cia.carga.model.UnidadeCarga;
import br.com.totvs.cia.importacao.model.CampoImportacao;
import br.com.totvs.cia.importacao.model.UnidadeImportacao;
import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.CampoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.ConfiguracaoPerfilConciliacao;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CO_UNIDADE_CONCILIACAO")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = false, of = {"id"})
public class UnidadeConciliacao implements Model {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-unidadeconciliacao-uuid")
	@GenericGenerator(name = "system-unidadeconciliacao-uuid", strategy = "uuid")
	private String id;
	
	@ManyToOne
	@JoinColumn(name="ID_LOTE_CONCILIACAO")
	private LoteConciliacao lote;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="unidade", orphanRemoval = true)
	private List<CampoChaveConciliacao> camposChave;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="unidade", orphanRemoval = true)
	private List<CampoConciliacao> camposConciliaveis;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="unidade", orphanRemoval = true)
	private List<CampoInformativo> camposInformativos;

	@Column(name = "STATUS")
	private StatusUnidadeConciliacaoEnum status;
	
	@Column(name = "JUSTIFICATIVA")
	private String justificativa;
	
	public UnidadeConciliacao(final LoteConciliacao lote) {
		this.lote = lote;
		this.lote.getUnidades().add(this);
	}
	
	public List<CampoChaveConciliacao> getCamposChave() {
		if (this.camposChave == null)
			this.camposChave = Lists.newArrayList();
		return this.camposChave;
	}
	
	public List<CampoConciliacao> getCamposConciliaveis() {
		if (this.camposConciliaveis == null) {
			this.camposConciliaveis = Lists.newArrayList();
		}
		return this.camposConciliaveis;
	}
	
	public List<CampoInformativo> getCamposInformativos() {
		if (this.camposInformativos == null) {
			this.camposInformativos = Lists.newArrayList();
		}
		return this.camposInformativos;
	}

	public void gerarChaves(final ConfiguracaoPerfilConciliacao configuracaoPerfil, final UnidadeCarga unidadeCarga) {
		for (final CampoPerfilConciliacao chave : configuracaoPerfil.getChaves()) {
			final CampoUnidadeCarga campo = unidadeCarga.getCampoPor(chave.getCampoCarga());
			final CampoChaveConciliacao campoChave = buildCampoChaveConciliacao(chave.getCampoImportacao().getCodigo(), campo, this);
			if (chave.getCampoEquivalente() != null) {
				final CampoUnidadeCarga campoCargaEquivalente = unidadeCarga.getCampoPor(chave.getCampoEquivalente());
				campoChave.comCampoEquivalente(chave.getCampoEquivalente(), campoCargaEquivalente);
			}
		}
	}
	
	public void gerarChaves(final ConfiguracaoPerfilConciliacao configuracaoPerfil, final UnidadeImportacao unidadeImportacao) {
		for (final CampoPerfilConciliacao chave : configuracaoPerfil.getChaves()) {
			final CampoImportacao campo = unidadeImportacao.getCampoPorDominio(chave.getCampoImportacao());
			final CampoChaveConciliacao campoChave = buildCampoChaveConciliacao(campo, this);
			if (chave.getCampoEquivalente() != null) {
				campoChave.comCampoEquivalenteVazio(chave.getCampoEquivalente());
			}
		}
	}

	public void gerarConciliacoes(final List<CampoPerfilConciliacao> camposConciliaveis, final UnidadeCarga unidadeCarga,
			final UnidadeImportacao unidadeImportacao) {
		
		for (final CampoPerfilConciliacao campoPerfilConciliacao : camposConciliaveis) {
			final CampoUnidadeCarga campoCarga = unidadeCarga.getCampoPor(campoPerfilConciliacao.getCampoCarga());
			final CampoImportacao campoImportacao = unidadeImportacao.getCampoPorDominio(campoPerfilConciliacao.getCampoImportacao());
			final CampoConciliacao campoConciliacao = buildCampoConciliacao(campoPerfilConciliacao.getCampoImportacao().getCodigo(), campoCarga, campoImportacao, this);
			if (campoPerfilConciliacao.getCampoEquivalente() != null) {
				final CampoUnidadeCarga campoCargaEquivalente = unidadeCarga.getCampoPor(campoPerfilConciliacao.getCampoEquivalente());
				campoConciliacao.comCampoEquivalente(campoPerfilConciliacao.getCampoEquivalente(), campoCargaEquivalente);
			}
		}
		
		this.gerarStatusUnidadeConciliacao();
		
	}

	public void gerarConciliacoes(final List<CampoPerfilConciliacao> camposConciliaveis, final UnidadeCarga unidadeCarga) {
		for (final CampoPerfilConciliacao campoPerfilConciliacao : camposConciliaveis) {
			final CampoUnidadeCarga campoCarga = unidadeCarga.getCampoPor(campoPerfilConciliacao.getCampoCarga());
			final CampoConciliacao campoConciliacao = buildCampoConciliacao(campoPerfilConciliacao.getCampoImportacao().getCodigo(), campoCarga, this);
			if (campoPerfilConciliacao.getCampoEquivalente() != null) {
				final CampoUnidadeCarga campoCargaEquivalente = unidadeCarga.getCampoPor(campoPerfilConciliacao.getCampoEquivalente());
				campoConciliacao.comCampoEquivalente(campoPerfilConciliacao.getCampoEquivalente(), campoCargaEquivalente);
			}
		}
		this.status = StatusUnidadeConciliacaoEnum.CHAVE_NAO_IDENTIFICADA;
		this.getLote().setStatusLote(StatusLoteConciliacaoEnum.DIVERGENTE);
	}

	public void gerarConciliacoes(final List<CampoPerfilConciliacao> camposConciliaveis,
			final UnidadeImportacao unidadeImportacao) {
		for (final CampoPerfilConciliacao campoPerfilConciliacao : camposConciliaveis) {
			final CampoImportacao campoImportacao = unidadeImportacao.getCampoPorDominio(campoPerfilConciliacao.getCampoImportacao());
			final CampoConciliacao campoConciliacao = buildCampoConciliacao(campoImportacao, this);
			if (campoPerfilConciliacao.getCampoEquivalente() != null) {
				campoConciliacao.comCampoEquivalente(campoPerfilConciliacao.getCampoEquivalente());
			}
		}
		this.status = StatusUnidadeConciliacaoEnum.CHAVE_NAO_IDENTIFICADA;
		this.getLote().setStatusLote(StatusLoteConciliacaoEnum.DIVERGENTE);
	}
	
	public void gerarCamposInformativos(final List<CampoPerfilConciliacao> camposInformativos, final UnidadeCarga unidadeCarga) {
		for (final CampoPerfilConciliacao campoPerfilInformativo : camposInformativos) {
			String valor = "";
			if (unidadeCarga != null) {
				final CampoUnidadeCarga campoCarga = unidadeCarga.getCampoPor(campoPerfilInformativo.getCampoCarga());
				valor = campoCarga.getValor();
			}
			buildCampo(campoPerfilInformativo, valor, this);
		}
		
	}
	
	private void gerarStatusUnidadeConciliacao() {
		final boolean statusDivergente = this.getCamposConciliaveis()
			.stream().anyMatch(campo -> !campo.getStatus().equals(StatusCampoConciliacaoEnum.OK));
		if (statusDivergente) {
			this.status = StatusUnidadeConciliacaoEnum.DIVERGENTE;
		} else {
			this.status = StatusUnidadeConciliacaoEnum.OK;
		}
	}

}
