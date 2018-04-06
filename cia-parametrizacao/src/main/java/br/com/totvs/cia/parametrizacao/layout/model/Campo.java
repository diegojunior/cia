package br.com.totvs.cia.parametrizacao.layout.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PA_CAMPO_LAYOUT")
@Data
@EqualsAndHashCode(callSuper = false, of = { "id" })
@AllArgsConstructor
@NoArgsConstructor
public class Campo implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-campo-uuid")
	@GenericGenerator(name = "system-campo-uuid", strategy = "uuid")
	private String id;
	
	private Integer ordem;

	@ManyToOne
	@JoinColumn(name = "ID_DOMINIO")
	private Dominio dominio;

	private String descricao;

	private String tag;

	private Integer tamanho;

	private Integer posicaoInicial;

	private Integer posicaoFinal;

	private String pattern;
	
	public Campo (final Integer ordem, final Dominio dominio) {
		this.ordem = ordem;
		this.dominio = dominio;
	}
	
	public Campo (final Integer ordem, final Dominio dominio, final String pattern) {
		this.ordem = ordem;
		this.dominio = dominio;
		this.pattern = pattern;
	}
	
	public Campo (final Integer ordem, final Dominio dominio, final String tag, final String pattern) {
		this.ordem = ordem;
		this.dominio = dominio;
		this.tag = tag;
		this.pattern = pattern;
	}
	
	public Campo (final Integer ordem, final Dominio dominio, final Integer tamanho, final Integer posicaoInicial, final Integer posicaoFinal) {
		this.ordem = ordem;
		this.dominio = dominio;
		this.tamanho = tamanho;
		this.posicaoInicial = posicaoInicial;
		this.posicaoFinal = posicaoFinal;
	}
}