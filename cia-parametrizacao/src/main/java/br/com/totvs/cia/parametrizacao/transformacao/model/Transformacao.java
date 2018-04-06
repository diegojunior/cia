package br.com.totvs.cia.parametrizacao.transformacao.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.layout.model.AbstractLayout;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.transformacao.json.TransformacaoJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PA_TRANSFORMACAO")
@EqualsAndHashCode(of={"id"})
public class Transformacao implements Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-transformacao-uuid")
	@GenericGenerator(name = "system-transformacao-uuid", strategy = "uuid")
	private String id;

	@Column(name="TIPO_LAYOUT")
	private TipoLayoutEnum tipoLayout;
	
	@ManyToOne(targetEntity = AbstractLayout.class)
	@JoinColumn(name = "ID_LAYOUT")
	private Layout layout;
	
	@ManyToOne
	@JoinColumn(name = "ID_SESSAO_LAYOUT")
	private Sessao sessao;

	@ManyToOne
	@JoinColumn(name = "ID_CAMPO_LAYOUT")
	private Campo campo;
	
	@Column(name="TIPO_TRANSFORMACAO")
	private TipoTransformacaoEnum tipoTransformacao;
	
	@OneToOne(targetEntity = AbstractItemTransformacao.class, cascade = CascadeType.ALL, mappedBy = "transformacao")
	private ItemTransformacao item;
	
	public Transformacao(final TransformacaoJson json, final Layout layout, final Sessao sessaoLayout,
			final Campo campoLayout, final ItemTransformacao item) {
		this.id = json.getId();
		this.tipoLayout = TipoLayoutEnum.fromCodigo(json.getTipoLayout().getCodigo());
		this.layout = layout;
		this.sessao = sessaoLayout;
		this.campo = campoLayout;
		this.tipoTransformacao = TipoTransformacaoEnum.fromCodigo(json.getTipoTransformacao().getCodigo());
		this.item = item.of(this);
	}
	
}