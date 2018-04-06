package br.com.totvs.cia.parametrizacao.layout.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PA_SESSAO_LAYOUT")
@ToString(callSuper = false, of = { "id", "codigo" })
@EqualsAndHashCode(callSuper = false, of = { "id", "codigo" })
public class Sessao implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-sessao-uuid")
	@GenericGenerator(name = "system-sessao-uuid", strategy = "uuid")
	private String id;

	private String nome;

	private String codigo;

	private int tamanho;
	
	private boolean semSessaoConfigurada;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "PA_SESSAO_CAMPO", joinColumns = @JoinColumn(name = "ID_SESSAO"), inverseJoinColumns = @JoinColumn(name = "ID_CAMPO"))
	private List<Campo> campos;
	
	public Sessao(final int tamanho) {
		this.tamanho = tamanho;
	}

	public List<Campo> getCampos() {
		if (this.campos == null) {
			this.campos = Lists.newArrayList();
		}
		return this.campos;
	}

	public void addCampo(final Campo campo) {
		if (this.campos == null) {
			this.campos = new ArrayList<Campo>();
		}
		this.campos.add(campo);
	}

	public Campo getCampoBy(final Campo campoParam) {

		if (campoParam.getId() == null)
			return null;

		return Iterables.tryFind(this.campos, new Predicate<Campo>() {

			@Override
			public boolean apply(final Campo campo) {
				return campoParam.getId().equals(campo.getId());
			}
		}).orNull();
	}
	
	public Campo getCampoBy(final String campoParam) {
		return Iterables.tryFind(this.campos, new Predicate<Campo>() {

			@Override
			public boolean apply(final Campo campo) {
				return campoParam.equals(campo.getDominio().getCodigo());
			}
		}).orNull();
	}

	public List<Dominio> getDominios() {
		return Lists.newArrayList(Lists.transform(this.getCampos(), new Function<Campo, Dominio>() {
			@Override
			public Dominio apply(final Campo campo) {
				return campo.getDominio();
			}
		}));
	}

}
