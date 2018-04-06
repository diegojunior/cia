package br.com.totvs.cia.importacao.model;

import static br.com.totvs.cia.parametrizacao.validacao.model.CampoValidacaoArquivoEnum.CORRETORA;
import static br.com.totvs.cia.parametrizacao.validacao.model.CampoValidacaoArquivoEnum.DATA;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.agente.model.Agente;
import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.model.Equivalencia;
import br.com.totvs.cia.cadastro.equivalencia.model.EquivalenciaTable;
import br.com.totvs.cia.cadastro.equivalencia.model.Remetente;
import br.com.totvs.cia.importacao.exception.ValidacaoInternaException;
import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.notificacao.model.StatusEnum;
import br.com.totvs.cia.parametrizacao.layout.model.AbstractLayout;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.validacao.model.AbstractValidacaoArquivo;
import br.com.totvs.cia.parametrizacao.validacao.model.ValidacaoArquivoInterno;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@Entity
@Table(name = "IM_IMPORTACAO")
@EqualsAndHashCode(callSuper = false, of = {"id"})
@ToString(callSuper = false, of = {"id"})
public class Importacao implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-importacao-uuid")
	@GenericGenerator(name = "system-importacao-uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "DATA_IMPORTACAO")
	@Temporal(TemporalType.DATE)
	private Date dataImportacao;

	@Column(name = "SISTEMA")
	private SistemaEnum sistema;
	
	@ManyToOne(targetEntity = AbstractLayout.class)
	@JoinColumn(name = "ID_LAYOUT")
	private Layout layout;
	
	@Column(name = "TIPO_IMPORTACAO")
	private TipoImportacaoEnum tipoImportacao;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ID_ARQUIVO")
	private Arquivo arquivo;
	
	@ManyToOne
	@JoinColumn(name = "ID_AGENTE")
	private Agente agente;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="ID_REMETENTE")
	private Remetente remetente;
	
	@Column(name = "STATUS", nullable = false)
	private StatusImportacaoEnum status;
	
	@ManyToMany(cascade = {CascadeType.ALL})
	@JoinTable(name = "IM_IMPORTACAO_EQUIV", 
				joinColumns = @JoinColumn(name = "ID_IMPORTACAO"),
				inverseJoinColumns = @JoinColumn(name = "ID_EQUIVALENCIA"))
	private List<Equivalencia> equivalencias;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "importacao", orphanRemoval = true)
	private List<UnidadeLayoutImportacao> unidades;
	
	public List<UnidadeLayoutImportacao> getUnidades() {
		if (this.unidades == null)
			this.unidades = Lists.newArrayList();
		return this.unidades;
	}
	
	public List<Equivalencia> getEquivalencias() {
		if (this.equivalencias == null)
			this.equivalencias = Lists.newArrayList();
		return this.equivalencias;
	}

	public void adicionarUnidadeImportacao(final UnidadeLayoutImportacao unidade) {
		unidade.setImportacao(this);
		this.getUnidades().add(unidade);
	}
	
	public boolean arquivoDesaprovadoPelaValidacaoInterna(final UnidadeLayoutImportacao unidade, final List<AbstractValidacaoArquivo> validacoesInternas) {
		final EquivalenciaTable equivalenciaTable = new EquivalenciaTable(this.getEquivalencias());
		for (final AbstractValidacaoArquivo abstractValidacao : validacoesInternas) {
			final ValidacaoArquivoInterno validacaoInterna = (ValidacaoArquivoInterno)abstractValidacao;
			if (unidade.getSessao().equals(validacaoInterna.getSessaoLayout())) {
				final CampoLayoutImportacao campoUnidade = unidade.getCampoPorDominio(validacaoInterna.getCampoLayout().getDominio());
				if (CORRETORA.equals(validacaoInterna.getCampoValidacao())) {
					if (!equivalenciaTable.contemEquivalencia(campoUnidade.getValor())) {
						return true;
					}
				} 
				
				if (DATA.equals(validacaoInterna.getCampoValidacao())) {
					try {
						final String formatoData = unidade.getSessao().getCampoBy(validacaoInterna.getCampoLayout()).getPattern();
						final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatoData);
						final LocalDate dataArquivo = LocalDate.parse(campoUnidade.getValor(), formatter);
						if (this.datasDiferentes(dataArquivo, this.dataImportacao)) {
							return true;
						}
					} catch (final DateTimeParseException e) {
						throw new ValidacaoInternaException("Erro ao executar a validação interna: O valor da data no arquivo segundo o formato no layout esta inválido.");
					}
				}
			}
		}
		return false;
	}

	private boolean datasDiferentes(final LocalDate dataArquivo, final Date dataImportacao) {
		final Calendar dateCalendar = Calendar.getInstance();
		dateCalendar.setTime(dataImportacao);
		final Instant instant = dateCalendar.toInstant();
		final LocalDate dataImportacaoLocalDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
		return !dataArquivo.isEqual(dataImportacaoLocalDate);
	}

	public void configura(final Importacao importacaoParam) {
		this.dataImportacao = importacaoParam.getDataImportacao();
		this.agente = importacaoParam.getAgente();
		this.arquivo = importacaoParam.getArquivo();
		this.sistema = importacaoParam.getSistema();
		this.remetente = importacaoParam.getRemetente();
		this.status = importacaoParam.getStatus();
		this.getEquivalencias().addAll(importacaoParam.getEquivalencias());
	}

	public void inicializaStatus() {
		this.status = StatusImportacaoEnum.ANDAMENTO;
	}

	public Importacao atualizaStatus(final StatusEnum status) {
		final StatusImportacaoEnum statusPeloCodigo = StatusImportacaoEnum.fromCodigo(status.getCodigo());
		this.status = Optional.fromNullable(statusPeloCodigo).or(StatusImportacaoEnum.ANDAMENTO);
		return this;
	}

}
