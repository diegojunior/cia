package br.com.totvs.cia.carga.model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.carga.json.LoteCargaJson;
import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CA_LOTE")
public class LoteCarga implements Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-lotecarga-uuid")
	@GenericGenerator(name = "system-lotecarga-uuid", strategy = "uuid")
	private String id;
	
	@ManyToOne
	@JoinColumn(name="ID_CARGA", nullable = false)
	private Carga carga;
	
	@ManyToOne
	@JoinColumn(name="ID_CONFIGURACAO_SERVICO", nullable = false)
	private ConfiguracaoServico servico;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="loteCarga", orphanRemoval = true)
	private List<LoteCliente> lotesClientes;
	
	@Column(name = "STATUS")
	private StatusLoteCargaEnum status;
	
	@Column(name = "EXECUCAO_ATIVADA")
	private Boolean isExecucaoAtivada;
	
	public LoteCarga(final Carga carga, final ConfiguracaoServico servico) {
		this.carga = carga;
		this.servico = servico;
		this.lotesClientes = Lists.newArrayList();
		this.status = StatusLoteCargaEnum.ANDAMENTO;
		this.isExecucaoAtivada = true;
	}
	
	public LoteCarga(final Carga carga, final ConfiguracaoServico servico, final List<String> clientes) {
		this.carga = carga;
		this.servico = servico;
		this.lotesClientes = LotesClientes.build(this, clientes);
		this.status = StatusLoteCargaEnum.ANDAMENTO;
		this.isExecucaoAtivada = true;
	}
	
	public LoteCarga(final LoteCargaJson json, final ConfiguracaoServico servico, final List<LoteCliente> lotesClientes) {
		this.id = json.getId();
		this.servico = servico;
		this.lotesClientes = lotesClientes;
	}
	
	public List<LoteCliente> getLotesClientes () {
		if (this.lotesClientes == null) {
			return Lists.newArrayList();
		}
		return this.lotesClientes;
	}
	
	public void addLoteCliente (final LoteCliente loteCliente) {
		if (this.lotesClientes == null) {
			this.lotesClientes = Lists.newArrayList();
		}
		this.lotesClientes.add(loteCliente);
	}
	
	public void addLotesClientes (final List<LoteCliente> lotesClientes) {
		if (this.lotesClientes == null) {
			this.lotesClientes = Lists.newArrayList();
		}
		this.lotesClientes.addAll(lotesClientes);
	}
	
	public List<String> getClientes() {
		final List<String> clientes = Lists.newArrayList();
		for (final LoteCliente loteCliente : this.lotesClientes) {
			clientes.add(loteCliente.getCliente());
		}
		return clientes;
	}

	public LoteCliente getLoteClienteBy(final String cliente) {
		for (final LoteCliente loteCliente : this.lotesClientes) {
			if (loteCliente.getCliente().trim().equalsIgnoreCase(cliente.trim())) {
				return loteCliente;
			}
		}
		return null;
	}
	
	public void ativaExecucao() {
		this.isExecucaoAtivada = true;
		if (this.lotesClientes != null){
			for (final LoteCliente loteCliente : this.lotesClientes) {
				loteCliente.setIsExecucaoAtivada(true);
			}
		}
	}
	
	public void inativaExecucao() {
		this.isExecucaoAtivada = false;
		if (this.lotesClientes != null){
			for (final LoteCliente loteCliente : this.lotesClientes) {
				loteCliente.setIsExecucaoAtivada(false);
			}
		}
	}

	public List<UnidadeCarga> getUnidadesCarga() {
		final List<UnidadeCarga> unidades = Lists.newArrayList();
		for (final List<UnidadeCarga> listUnidades : this.getLotesClientes()
					.stream()
					.map(loteCarteira -> loteCarteira.getUnidades())
					.collect(Collectors.toList())) {
			unidades.addAll(listUnidades);
		}
		Collections.sort(unidades);
		return unidades;
	}
	
	public List<String> getClientesComExecucaoAtivada() {
		final List<String> clientes = Lists.newArrayList();
		for (final LoteCliente loteCliente : this.lotesClientes) {
			if (loteCliente.getIsExecucaoAtivada()) {
				clientes.add(loteCliente.getCliente());
			}
		}
		return clientes;
	}
	
	public List<LoteCliente> getLotesClientesComExecucaoAtivada() {
		return this.getLotesClientes().stream().filter(lote -> lote.getIsExecucaoAtivada()).collect(Collectors.toList());
	}

	public void atualizaStatusParaErro() {
		this.status = StatusLoteCargaEnum.ERRO;
	}

	public void atualizaStatusParaSucesso() {
		this.status = StatusLoteCargaEnum.CONCLUIDO;
	}

	public void atualizaStatusLotesClientes(final List<LoteCliente> lotesClientesAlterados) {
		for (LoteCliente loteClienteAlterado : lotesClientesAlterados) {
			for (LoteCliente loteCliente : this.lotesClientes) {
				if (loteClienteAlterado.getId().equals(loteCliente.getId())) {
					loteCliente.setStatus(loteClienteAlterado.getStatus());
					break;
				}
			}
		}
	}
}