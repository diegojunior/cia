package br.com.totvs.cia.parametrizacao.transformacao.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.equivalencia.model.Remetente;
import br.com.totvs.cia.cadastro.equivalencia.model.TipoEquivalencia;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "ID")
@Table(name = "PA_ITEM_TRANS_EQUIVA")
@EqualsAndHashCode(callSuper = false)
public class ItemTransformacaoEquivalencia extends AbstractItemTransformacao {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "SISTEMA")
	private SistemaEnum sistema;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="ID_REMETENTE")
	private Remetente remetente;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="ID_TIPO_EQUIVALENCIA")
	private TipoEquivalencia tipoEquivalencia;
	
	public ItemTransformacaoEquivalencia(final String id, final Transformacao transformacao, 
			final SistemaEnum sistema, final Remetente remetente, final TipoEquivalencia tipoEquivalencia) {
		super(id, transformacao);
		this.sistema = sistema;
		this.remetente = remetente;
		this.tipoEquivalencia = tipoEquivalencia;
	}
	
	public ItemTransformacaoEquivalencia(final String id, final SistemaEnum sistema, 
			final Remetente remetente, final TipoEquivalencia tipoEquivalencia) {
		super(id);
		this.sistema = sistema;
		this.remetente = remetente;
		this.tipoEquivalencia = tipoEquivalencia;
	}
	
	@Override
	public ItemTransformacao of(final Transformacao transformacao) {
		this.setTransformacao(transformacao);
		return this;
	}
	
	@Override
	public Boolean isTipoTransformacaoFixo() {
		return false;
	}
	
	@Override
	public Boolean isTipoTransformacaoEquivalencia() {
		return true;
	}
}