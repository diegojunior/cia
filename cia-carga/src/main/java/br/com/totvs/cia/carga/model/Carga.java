package br.com.totvs.cia.carga.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.cadastro.configuracaoservico.model.ServicoEnum;
import br.com.totvs.cia.carga.json.CargaExecucaoJson;
import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.infra.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CA_CARGA")
@EqualsAndHashCode(callSuper = false, of = {"id"})
public class Carga implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-carga-uuid")
	@GenericGenerator(name = "system-carga-uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "DATA_POSICAO", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date data;

	@Column(name = "SISTEMA", nullable = false)
	private SistemaEnum sistema;
	
	@Column(name = "STATUS", nullable = false)
	private StatusCargaEnum status;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="carga", orphanRemoval = true)
	private List<LoteCarga> lotes;

	public Carga (final CargaExecucaoJson cargaJson, final List<ConfiguracaoServico> servicos, 
			final List<String> clientes) throws Exception {
		this.id = cargaJson.getId();
		this.data = DateUtil.parse(cargaJson.getData(), DateUtil.yyyy_MM_dd);
		this.sistema = SistemaEnum.fromCodigo(cargaJson.getSistema().getCodigo());
		this.lotes = LotesCarga.build(this, servicos, clientes);
		this.status = StatusCargaEnum.ANDAMENTO;
	}
	
	public Carga (final CargaExecucaoJson cargaJson, final List<ConfiguracaoServico> servicos) throws Exception {
		this.id = cargaJson.getId();
		this.data = DateUtil.parse(cargaJson.getData(), DateUtil.yyyy_MM_dd);
		this.sistema = SistemaEnum.fromCodigo(cargaJson.getSistema().getCodigo());
		this.lotes = LotesCarga.build(this, servicos);
		this.status = StatusCargaEnum.ANDAMENTO;
	}

	public Boolean isEmpty() {
		return this.lotes.isEmpty();
	}

	public void criaNovosLotesClientes(final LoteCarga loteParam) {
		for (final LoteCarga lote : this.lotes) {
			if (lote.getServico().equals(loteParam.getServico())) {
				lote.setIsExecucaoAtivada(true);
				for (final LoteCliente loteClienteParam : loteParam.getLotesClientes()) {
					if (!lote.getClientes().contains(loteClienteParam.getCliente())) {
						loteClienteParam.setLoteCarga(lote);
						loteClienteParam.setStatus(StatusLoteClienteEnum.ATENCAO);
						lote.getLotesClientes().add(loteClienteParam);
					}
				}
				break;
			}
		}
	}
	
	public void ativaReexecucao (final LoteCarga loteParam) {
		for (final LoteCarga lote : this.lotes) {
			if (lote.getServico().equals(loteParam.getServico())) {
				for (final LoteCliente loteCarteira : lote.getLotesClientes()) {
					loteCarteira.setIsExecucaoAtivada(false);
					if (loteParam.getClientes().contains(loteCarteira.getCliente())) {
						loteCarteira.setIsExecucaoAtivada(true);
						loteCarteira.getUnidades().clear();
					}
				}
				break;
			}
		}
	}

	public LoteCarga getLote(final ConfiguracaoServico servico) {
		for (final LoteCarga lote : this.lotes) {
			if (lote.getServico().equals(servico)) {
				return lote;
			}
		}
		return null;
	}
	
	public LoteCarga getLote(final ServicoEnum servico) {
		for (final LoteCarga lote : this.lotes) {
			if (lote.getServico().getServico().equals(servico)) {
				return lote;
			}
		}
		return null;
	}
	
	public List<String> getCarteiras () {
		final List<String> clientes = Lists.newArrayList();
		for (final LoteCarga lote : this.lotes) {
			clientes.addAll(lote.getClientes());
		}
		return clientes;
	}

	public LoteCliente getLoteClienteBy(final ServicoEnum servico, final String carteira) {
		for (final LoteCarga lote : this.lotes) {
			if (lote.getServico().getServico().equals(servico)) {
				return lote.getLoteClienteBy(carteira);
			}
		}
		return null;
	}
	
	public void ativaExecucaoLotes() {
		for (final LoteCarga lote : this.lotes) {
			lote.ativaExecucao();
		}
	}

	public void inativaExecucaoLotes() {
		for (final LoteCarga lote : this.lotes) {
			lote.inativaExecucao();
		}
	}

	public Boolean isLotesEmAndamento() {
		for (final LoteCarga lote : this.lotes) {
			if (lote.getStatus().isAndamento()) {
				return true;
			}
		}
		return false;
	}

	public void setStatusLotes(StatusLoteCargaEnum status) {
		for (LoteCarga lote : this.lotes) {
			lote.setStatus(status);
		}
	}
}