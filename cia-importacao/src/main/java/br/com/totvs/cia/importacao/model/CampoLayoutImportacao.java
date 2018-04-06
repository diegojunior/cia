package br.com.totvs.cia.importacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.infra.Campo;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "IM_CAMPO_LAYOUT_IMPORTACAO")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false, of = {"campo", "valor"})
@ToString(callSuper = false, of = { "campo", "valor" })
@Getter
@Setter
public class CampoLayoutImportacao implements Campo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-campolayoutimportacao-uuid")
	@GenericGenerator(name = "system-campolayoutimportacao-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name = "ID_DOMINIO")
	private Dominio campo;

	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_LAYOUT_IMPORTACAO")
	private UnidadeLayoutImportacao unidade;

	@Column(name = "VALOR", length = 500)
	private String valor;
	
	@Column(name = "PATTERN")
	private String pattern;

	public CampoLayoutImportacao(final Dominio campo, final String valor) {
		this.campo = campo;
		this.valor = valor;
	}

	public CampoLayoutImportacao(final Dominio campo, final UnidadeLayoutImportacao unidade, final String valor) {
		this.campo = campo;
		this.valor = valor;
		this.unidade = unidade;
	}

	@Override
	public void atualizaValor(final String novoValor) {
		this.valor = novoValor;

	}

	@Override
	public String getValor() {
		return this.valor;
	}

}
