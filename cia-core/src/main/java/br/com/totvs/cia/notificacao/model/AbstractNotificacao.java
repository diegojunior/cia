package br.com.totvs.cia.notificacao.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.GenericGenerator;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "NO_NOTIFICACAO")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = {"id"})
@Getter
public abstract class AbstractNotificacao implements Notificacao {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-notificacao-uuid")
	@GenericGenerator(name = "system-notificacao-uuid", strategy = "uuid")
	private String id;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DATA")
	private Date data;
	
	@Column(name = "STATUS")
	private StatusEnum status;
	
	@Column(name = "MENSAGEM", length = 4000)
	private String mensagem;
	
	public AbstractNotificacao (final Date data, final StatusEnum status, final String mensagem){
		this.data = data;
		this.status = status;
		this.mensagem = mensagem;
	}
}