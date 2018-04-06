package br.com.totvs.cia.importacao.model;

import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
@Entity
@Table(name = "IM_UNIDADE_LAYOUT_IMPORTACAO")
@Getter
@Setter
@ToString(callSuper = false, of = { "id" })
public class UnidadeLayoutImportacao implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-unidadelayoutimportacao-uuid")
	@GenericGenerator(name = "system-unidadelayoutimportacao-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name = "ID_IMPORTACAO")
	private Importacao importacao;

	@ManyToOne
	@JoinColumn(name = "ID_SESSAO")
	private Sessao sessao;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "unidade", orphanRemoval = true)
	private List<CampoLayoutImportacao> campos;
	
	@Column(name = "NUMERO_LINHA")
	private Integer numeroLinha;
	
	@Column(name = "NUMERO_LINHA_ANTERIOR")
	private Integer numeroLinhaAnterior;

	public List<CampoLayoutImportacao> getCampos() {
		if (this.campos == null)
			this.campos = Lists.newArrayList();
		return this.campos;
	}

	public CampoLayoutImportacao getCampoPorDominio(final Dominio dominio) {
		return Iterables.find(this.getCampos(), new Predicate<CampoLayoutImportacao>() {
			@Override
			public boolean apply(final CampoLayoutImportacao input) {
				return input.getCampo().equals(dominio);
			}
		});
	}
	
	public List<CampoLayoutImportacao> getCamposLayoutImportacaoPorCampo(final List<Campo> campos) {
		List<CampoLayoutImportacao> camposLayoutImportacao = Lists.newArrayList();
		for (Campo campo : campos) {
			camposLayoutImportacao.add(this.getCampoPorDominio(campo.getDominio()));
		}
		return camposLayoutImportacao;
	}
	
	public Map<Campo, CampoLayoutImportacao> getCamposIguaisDe(final List<Campo> camposParam) {
		final Map<Campo, CampoLayoutImportacao> campos = Maps.newHashMap();
		for (final Campo campo : camposParam) {
			final CampoLayoutImportacao campoLayoutImportacao = Iterables.tryFind(this.campos, new Predicate<CampoLayoutImportacao>() {

				@Override
				public boolean apply(final CampoLayoutImportacao input) {
					return input.getCampo().equals(campo.getDominio());
				}
			}).orNull();
			
			if (campoLayoutImportacao != null) {
				campos.put(campo, campoLayoutImportacao);
			}

		}
		return campos;
	}
	
	
}
