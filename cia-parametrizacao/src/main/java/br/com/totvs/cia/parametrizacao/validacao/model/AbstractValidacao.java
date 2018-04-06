package br.com.totvs.cia.parametrizacao.validacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.parametrizacao.layout.model.AbstractLayout;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PA_VALIDACAO")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractValidacao implements Validacao {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-validacao-uuid")
	@GenericGenerator(name = "system-validacao-uuid", strategy = "uuid")
	private String id;
	
	@Column(name="TIPO_VALIDACAO")
	private TipoValidacaoEnum tipoValidacao;
	
	@Column(name="TIPO_LAYOUT")
	private TipoLayoutEnum tipoLayout;

	@ManyToOne(targetEntity = AbstractLayout.class)
	@JoinColumn(name = "ID_LAYOUT")
	private Layout layout;
	
}