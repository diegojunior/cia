package br.com.totvs.cia.conciliacao.model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.carga.model.Carga;
import br.com.totvs.cia.carga.model.LoteCarga;
import br.com.totvs.cia.importacao.model.Importacao;
import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "CO_CONCILIACAO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Conciliacao implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-conciliacao-uuid")
	@GenericGenerator(name = "system-conciliacao-uuid", strategy = "uuid")
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA", nullable = false)
	private Date data;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_EXECUCAO", nullable = false)
	private Date dataExecucao;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA_GRAVACAO", nullable = true)
	private Date dataGravacao;
	
	@ManyToOne
	@JoinColumn(name="ID_PERFIL_CONCILIACAO")
	private PerfilConciliacao perfil;
	
	@ManyToOne
	@JoinColumn(name="ID_CARGA")
	private Carga carga;
	
	@ManyToOne
	@JoinColumn(name="ID_IMPORTACAO")
	private Importacao importacao;
	
	@Column(name = "STATUS", nullable = false)
	private StatusConciliacaoEnum status;
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy="conciliacao")
	private LoteConciliacao lote;
	
	public Conciliacao(final Date data, final PerfilConciliacao perfil, 
			final Importacao importacao, final Carga carga, final Date dataExecucao) {
		this.data = data;
		this.perfil = perfil;
		this.carga = carga;
		this.importacao = importacao;
		this.dataExecucao = dataExecucao;
	}

	public Boolean isDivergente() {
		return this.status.isDivergente();
	}
	
	public Boolean isGravada() {
		return this.status.isGravada();
	}
	
	public Boolean isErro() {
		return this.status.isErro();
	}
	
	public LoteCarga getLoteServicoConfiguradoParaCarga(final ConfiguracaoServico servico) {
		if (this.carga != null) {
			return Iterables.tryFind(this.carga.getLotes(), new Predicate<LoteCarga>() {
				@Override
				public boolean apply(final LoteCarga lote) {
					return lote.getServico().equals(servico);
				}
			}).orNull();
			
		}
		return null;
	}

	public void comDataExecucao(final Date date) {
		this.dataExecucao = date;
		
	}

	public void comStatus(final StatusConciliacaoEnum status) {
		this.status = status;
	}
	
	public boolean possuiLoteDivergente() {
		return this.getLote().getStatusLote().equals(StatusLoteConciliacaoEnum.DIVERGENTE);
	}
}