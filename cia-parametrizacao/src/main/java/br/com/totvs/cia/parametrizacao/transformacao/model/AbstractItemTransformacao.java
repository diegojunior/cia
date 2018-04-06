package br.com.totvs.cia.parametrizacao.transformacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PA_ITEM_TRANSFORMACAO")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractItemTransformacao implements ItemTransformacao {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-itemtransformacao-uuid")
	@GenericGenerator(name = "system-itemtransformacao-uuid", strategy = "uuid")
	private String id;
	
	@OneToOne
	@JoinColumn(name = "ID_TRANSFORMACAO")
	private Transformacao transformacao;
	
	public AbstractItemTransformacao(final String id) {
		this.id = id;
		this.transformacao = null;
	}

}