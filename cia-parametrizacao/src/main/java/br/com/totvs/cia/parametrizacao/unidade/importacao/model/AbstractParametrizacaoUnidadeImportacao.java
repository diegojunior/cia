package br.com.totvs.cia.parametrizacao.unidade.importacao.model;

import java.util.List;

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

import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.layout.model.AbstractLayout;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PA_UNIDADE_IMPORTACAO")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractParametrizacaoUnidadeImportacao implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-absparametrizacaoundimp-uuid")
	@GenericGenerator(name = "system-absparametrizacaoundimp-uuid", strategy = "uuid")
	private String id;

	@Column(name = "CODIGO", length = 30, unique = true)
	private String codigo;

	@Column(name = "DESCRICAO", length = 60)
	private String descricao;

	@Column(name = "TIPO_LAYOUT")
	private TipoLayoutEnum tipoLayout;

	@ManyToOne(targetEntity = AbstractLayout.class)
	@JoinColumn(name = "ID_LAYOUT")
	private Layout layout;

	public abstract Campo getCampoBy(final String campo);
	
	public List<Campo> getCampos() {
		if (this.tipoLayout.isDelimitador()) {
			return ((ParametrizacaoUnidadeImportacaoBloco)this).getCamposLayout();
		}
		return ((ParametrizacaoUnidadeImportacaoChave)this).getCamposLayout();
	}
}