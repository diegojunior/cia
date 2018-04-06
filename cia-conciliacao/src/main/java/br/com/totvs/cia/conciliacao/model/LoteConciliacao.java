package br.com.totvs.cia.conciliacao.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "CO_LOTE_CONCILIACAO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = false, of = {"id"})
public class LoteConciliacao implements Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-loteconciliacao-uuid")
	@GenericGenerator(name = "system-loteconciliacao-uuid", strategy = "uuid")
	private String id;
	
	@OneToOne
	@JoinColumn(name="ID_CONCILIACAO")
	private Conciliacao conciliacao;
	
	@Column(name = "STATUS")
	private StatusLoteConciliacaoEnum statusLote;

	@OneToMany(cascade = CascadeType.ALL, mappedBy="lote", orphanRemoval = true)
	private List<UnidadeConciliacao> unidades;
	
	public List<UnidadeConciliacao> getUnidades() {
		if (this.unidades == null)
			this.unidades = Lists.newArrayList();
		return this.unidades;
	}

	public static LoteConciliacao buildLoteConciliacao(final Conciliacao conciliacao) {
		final LoteConciliacao loteConciliacao = new LoteConciliacao();
		loteConciliacao.setConciliacao(conciliacao);
		conciliacao.setLote(loteConciliacao);
		return loteConciliacao;
	}
	
	public int quantidadeUnidadeDivergente() {
		return this.getUnidades()
				.stream()
				.filter(unidade -> StatusUnidadeConciliacaoEnum.DIVERGENTE.equals(unidade.getStatus()))
				.collect(Collectors.counting())
				.intValue();
	}
	
	public int quantidadeUnidadeOk() {
		return this.getUnidades()
				.stream()
				.filter(unidade -> StatusUnidadeConciliacaoEnum.OK.equals(unidade.getStatus()))
				.collect(Collectors.counting())
				.intValue();
	}
	
	public int quantidadeUnidadeChaveNaoIdentificada() {
		return this.getUnidades()
				.stream()
				.filter(unidade -> StatusUnidadeConciliacaoEnum.CHAVE_NAO_IDENTIFICADA.equals(unidade.getStatus()))
				.collect(Collectors.counting())
				.intValue();
	}
	
	public boolean possuiUnidadeComStatusDiferenteOK() {
		if (this.getUnidades().isEmpty()) {
			return false;
		}
		return this.getUnidades()
					.stream()
					.anyMatch(unidade -> !unidade.getStatus().equals(StatusUnidadeConciliacaoEnum.OK));
	}
}