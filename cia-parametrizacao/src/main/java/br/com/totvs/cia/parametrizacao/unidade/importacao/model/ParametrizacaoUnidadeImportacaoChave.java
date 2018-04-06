package br.com.totvs.cia.parametrizacao.unidade.importacao.model;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "PA_UNIDADE_IMPORTACAO_CHAVE")
@PrimaryKeyJoinColumn(name = "ID")
public class ParametrizacaoUnidadeImportacaoChave extends AbstractParametrizacaoUnidadeImportacao {

	private static final long serialVersionUID = 1L;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "PA_UNIDADE_CHAVE_SESSAO", 
		joinColumns = @JoinColumn(name = "ID_UNIDADE_CHAVE"), 
		inverseJoinColumns = @JoinColumn(name = "ID_SESSAO"))
	private List<Sessao> sessoes;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "param")
	private Set<CampoParametrizacaoUnidadeImportacao> camposUnidadeImportacao;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "param")
	private Set<ChaveParametrizacaoUnidadeImportacao> chaves;
	
	public List<Sessao> getSessoes() {
		if (this.sessoes == null) {
			this.sessoes = Lists.newArrayList();
		}
		return this.sessoes;
	}
	
	public Set<CampoParametrizacaoUnidadeImportacao> getCamposUnidadeImportacao() {
		if (this.camposUnidadeImportacao == null) {
			this.camposUnidadeImportacao = Sets.newHashSet();
		}
		return this.camposUnidadeImportacao;
	}

	public Set<ChaveParametrizacaoUnidadeImportacao> getChaves() {
		if (this.chaves == null) {
			this.chaves = Sets.newHashSet();
		}
		return this.chaves;
	}

	public void atualizarCampoUnidade(final CampoParametrizacaoUnidadeImportacao campo) {
		final CampoParametrizacaoUnidadeImportacao found = Iterables
				.tryFind(this.getCamposUnidadeImportacao(), new Predicate<CampoParametrizacaoUnidadeImportacao>() {

					@Override
					public boolean apply(final CampoParametrizacaoUnidadeImportacao param) {
						if (param.getId() != null) {
							if (campo.getId() != null) {
								if (param.getId().equals(campo.getId())) {
									return true;
								}
							}
						}
						return false;
					}
				}).orNull();

		if (found == null) {
			campo.setParam(this);
			this.getCamposUnidadeImportacao().add(campo);
		}

	}
	
	public void atualizarSessao(final Sessao sessao) {
		final Sessao found = Iterables.tryFind(this.getSessoes(), new Predicate<Sessao>() {

			@Override
			public boolean apply(final Sessao input) {
				return input.equals(sessao);
			}
		}).orNull();
		
		if (found == null) this.getSessoes().add(sessao);
		
	}
	
	public void atualizarChaveUnidade(final ChaveParametrizacaoUnidadeImportacao chave) {
		final ChaveParametrizacaoUnidadeImportacao found = Iterables
				.tryFind(this.getChaves(), new Predicate<ChaveParametrizacaoUnidadeImportacao>() {

					@Override
					public boolean apply(final ChaveParametrizacaoUnidadeImportacao param) {
						if (param.getId() != null) {
							if (chave.getId() != null) {
								if (param.getId().equals(chave.getId())) {
									return true;
								}
							}
						}
						return false;
					}
				}).orNull();

		if (found == null)  {
			chave.setParam(this);
			this.getChaves().add(chave);
		}

	}

	public Set<CampoParametrizacaoUnidadeImportacao> getCamposUnidadeImportacaoPorSessao(final Sessao sessao) {
		return Sets.filter(this.getCamposUnidadeImportacao(), new Predicate<CampoParametrizacaoUnidadeImportacao>() {

			@Override
			public boolean apply(final CampoParametrizacaoUnidadeImportacao campo) {
				return campo.getSessao().equals(sessao);
			}
		});
	}
	
	public List<Campo> getCamposLayout() {
		final List<Campo> campos = Lists.newArrayList();
		campos.addAll(this.getCamposChaves());
		campos.addAll(this.getDemaisCampos());
		return campos;
	}

	public Set<Campo> getCamposChaves() {
		final Set<Campo> camposChaves = Sets.newHashSet();

		for (final ChaveParametrizacaoUnidadeImportacao chave : this.getChaves()) {
			camposChaves.add(chave.getCampo());
		}
		
		return camposChaves;
	}
	
	public Set<Campo> getDemaisCampos() {
		final Set<Campo> demaisCampos= Sets.newHashSet();
		for (final CampoParametrizacaoUnidadeImportacao campo : this.getCamposUnidadeImportacao()) {
			demaisCampos.add(campo.getCampo());
		}
		return demaisCampos;
	}

	public List<Sessao> getSessoesChave() {
		for (final ChaveParametrizacaoUnidadeImportacao chave : this.getChaves()) {
			return chave.getSessoes();
		}
		return Lists.newArrayList();
	}

	public void clearSessoes() {
		this.getSessoes().clear();
		
	}

	public void clearChaves() {
		this.getChaves().forEach(action -> {
			action.getSessoes().clear();
		});
		
	}

	@Override
	public Campo getCampoBy(final String codigoCampo) {
		for (Campo campo : this.getCamposLayout()) {
			if (campo.getDominio().getCodigo().equalsIgnoreCase(codigoCampo)) {
				return campo;
			}
		}
		return null;
	}

	public int getTotalCamposParametrizados() {
		return this.getCamposChaves().size() + this.getDemaisCampos().size();
	}
}