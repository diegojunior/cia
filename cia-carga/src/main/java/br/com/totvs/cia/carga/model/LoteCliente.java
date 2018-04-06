package br.com.totvs.cia.carga.model;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.carga.json.LoteClienteJson;
import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CA_LOTE_CLIENTE")
public class LoteCliente implements Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-lotecliente-uuid")
	@GenericGenerator(name = "system-lotecliente-uuid", strategy = "uuid")
	private String id;
	
	@ManyToOne
	@JoinColumn(name="ID_LOTE_CARGA", nullable = false)
	private LoteCarga loteCarga;
	
	@Column(name = "CLIENTE")
	private String cliente;
	
	@Column(name = "DATA_PROCESSAMENTO")
	@Temporal(TemporalType.DATE)
	private Date dataProcessamento;

	@Column(name = "STATUS")
	private StatusLoteClienteEnum status;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="loteCliente", orphanRemoval = true)
	private List<UnidadeCarga> unidades;
	
	@Column(name = "EXECUCAO_ATIVADA")
	private Boolean isExecucaoAtivada;
	
	public LoteCliente(final LoteCarga loteCarga, final String cliente) {
		this.loteCarga = loteCarga;
		this.cliente = cliente;
		this.dataProcessamento = new Date();
		this.unidades = Lists.newArrayList();
		this.status = StatusLoteClienteEnum.ATENCAO;
		this.isExecucaoAtivada = true;
	}
	
	public LoteCliente(final LoteCarga loteCarga, final String cliente, final StatusLoteClienteEnum status) {		
		this.loteCarga = loteCarga;
		this.cliente = cliente;
		this.dataProcessamento = new Date();
		this.unidades = Lists.newArrayList();
		this.status = status;
		this.isExecucaoAtivada = true;
	}
	
	public ConfiguracaoServico getServico () {
		return this.loteCarga.getServico();
	}

	public LoteCliente(final LoteClienteJson json, final String cliente) {
		this.id = json.getId();
		this.cliente = cliente;
	}

	public Boolean contemUnidades() {
		return this.unidades != null && !this.unidades.isEmpty();
	}
}