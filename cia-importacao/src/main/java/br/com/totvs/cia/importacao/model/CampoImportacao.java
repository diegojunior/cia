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
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@Table(name = "IM_CAMPO_IMPORTACAO")
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = false, of = { "campo", "valor" })
public class CampoImportacao implements Campo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-campoimportacao-uuid")
	@GenericGenerator(name = "system-campoimportacao-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name = "ID_DOMINIO")
	private Dominio campo;

	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_IMPORTACAO")
	private UnidadeImportacao unidade;

	@Column(name = "VALOR", length = 500)
	private String valor;
	
	@Column(name = "PATTERN")
	private String pattern;
	
	private transient String valorConsolidado;

	public CampoImportacao(final Dominio campo, final String valor, final String pattern) {
		this.campo = campo;
		this.valor = valor;
		this.pattern = pattern;
	}

	public CampoImportacao(final Dominio campo, final UnidadeImportacao unidade, final String valor, final String pattern) {
		this.campo = campo;
		this.valor = valor;
		this.unidade = unidade;
		this.pattern = pattern;
	}

	@Override
	public void atualizaValor(final String novoValor) {
		this.valor = novoValor;

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.campo == null) ? 0 : this.campo.hashCode());
		result = prime * result + ((this.valor == null) ? 0 : this.valor.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final CampoImportacao other = (CampoImportacao) obj;
		if (this.campo == null) {
			if (other.campo != null)
				return false;
		} else if (!this.campo.equals(other.campo))
			return false;
		if (this.valor == null) {
			if (other.valor != null)
				return false;
		} else if (!this.valor.equals(other.valor))
			return false;
		return true;
	}

	@Override
	public String getValor() {
		if (this.valorConsolidado != null)
			return this.valorConsolidado;
		return this.valor;
	}

	public void atualizaValorConsolidado(final String valorConsolidado) {
		this.valorConsolidado = valorConsolidado;
	}

}
