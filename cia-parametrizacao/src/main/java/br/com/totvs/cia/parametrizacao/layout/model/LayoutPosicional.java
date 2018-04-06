package br.com.totvs.cia.parametrizacao.layout.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "PA_LAYOUT_POSICIONAL")
@PrimaryKeyJoinColumn(name = "ID")
public class LayoutPosicional extends AbstractLayout {

	private static final long serialVersionUID = 1L;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "PA_LAYOUT_POS_SESSAO", joinColumns = @JoinColumn(name = "ID_LAYOUT_POS"), inverseJoinColumns = @JoinColumn(name = "ID_SESSAO"))
	private List<Sessao> sessoes;

	public void addSessao(final Sessao sessao) {
		if (this.sessoes == null) {
			this.sessoes = new ArrayList<Sessao>();
		}
		this.sessoes.add(sessao);
	}

	@Override
	public List<Sessao> getSessoes() {
		if (this.sessoes == null) {
			this.sessoes = Lists.newArrayList();
		}
		return this.sessoes;
	}

	public Sessao getSessaoById(final String id) {
		return Iterables.tryFind(this.getSessoes(), new Predicate<Sessao>() {
			@Override
			public boolean apply(final Sessao sessao) {
				if (id != null) {
					return id.equals(sessao.getId());
				}
				return false;
			}
		}).orNull();
	}

	public Sessao getSessaoBy(final String codigo) {
		return Iterables.tryFind(this.getSessoes(), new Predicate<Sessao>() {
			@Override
			public boolean apply(final Sessao sessao) {
				return codigo.equals(sessao.getCodigo());
			}
		}).orNull();
	}

	@Override
	public List<Dominio> getDominios() {
		List<Dominio> dominios = Lists.newArrayList();
		for (Sessao sessao : this.getSessoes()) {
			dominios.addAll(sessao.getDominios());
		}
		return dominios;
	}
}
