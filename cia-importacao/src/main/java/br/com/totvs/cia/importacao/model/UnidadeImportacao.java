package br.com.totvs.cia.importacao.model;

import java.math.BigDecimal;
import java.util.Collection;
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

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.infra.util.Constants;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.infra.util.NumberUtil;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.CampoPerfilConciliacao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoChave;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "IM_UNIDADE_IMPORTACAO")
@ToString(callSuper = false, of = { "id" })
public class UnidadeImportacao implements Model, Comparable<UnidadeImportacao> {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-unidadeimportacao-uuid")
	@GenericGenerator(name = "system-unidadeimportacao-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name = "ID_IMPORTACAO")
	private Importacao importacao;
	
	@ManyToOne
	@JoinColumn(name = "ID_SESSAO")
	private Sessao sessao;
	
	@ManyToOne
	@JoinColumn(name = "ID_PARAMETRIZACAO")
	private ParametrizacaoUnidadeImportacaoChave parametrizacaoUnidade;
	
	@ManyToOne
	@JoinColumn(name = "ID_PARAMETRIZACAO_BLOCO")
	private ParametrizacaoUnidadeImportacaoBloco parametrizacaoBlocoUnidade;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "unidade", orphanRemoval = true)
	private List<CampoImportacao> campos;

	public List<CampoImportacao> getCampos() {
		if (this.campos == null)
			this.campos = Lists.newArrayList();
		return this.campos;
	}
	
	public void adicionarParametrizacaoUnidade(final ParametrizacaoUnidadeImportacaoChave parametrizacaoUnidadeImportacao) {
		this.parametrizacaoUnidade = parametrizacaoUnidadeImportacao;
	}
	
	public void adicionarParametrizacaoUnidadeBloco(final ParametrizacaoUnidadeImportacaoBloco parametrizacaoBlocoUnidade) {
		this.parametrizacaoBlocoUnidade = parametrizacaoBlocoUnidade;
	}
	
	public void adicionarSessao(final Sessao sessao) {
		this.sessao = sessao;
	}

	public CampoImportacao getCampoPorDominio(final Dominio dominio) {
		return Iterables.find(this.getCampos(), new Predicate<CampoImportacao>() {
			@Override
			public boolean apply(final CampoImportacao input) {
				return input.getCampo().equals(dominio);
			}
		});
	}
	
	public void convertAndAdd(final Collection<CampoLayoutImportacao> campos) {
		Collection<CampoImportacao> camposTransformados = Collections2.transform(campos, new Function<CampoLayoutImportacao, CampoImportacao>() {
			@Override
			public CampoImportacao apply(final CampoLayoutImportacao input) {
				return new CampoImportacao(input.getCampo(), UnidadeImportacao.this, input.getValor(), input.getPattern());
			}
		});
		this.getCampos().addAll(camposTransformados);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		final UnidadeImportacao other = (UnidadeImportacao) obj;
		if (this.campos == null) {
			if (other.campos != null)
				return false;
		} else if (!this.campos.equals(other.campos))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((this.campos == null) ? 0 : this.campos.hashCode());
		return result;
	}
	
	public Integer getHashChave(final Map<Dominio, CampoPerfilConciliacao> chaves) {
		int result = 1;
		for (final Dominio campo : chaves.keySet()) {
			final CampoImportacao chaveImportacao = this.getCampoPorDominio(campo);
			if (campo.isDate()) {
				String transformToYYYYMMDD = DateUtil.transformToYYYYMMDD(chaveImportacao.getValor());
				chaveImportacao.atualizaValor(transformToYYYYMMDD);
				result = result + chaveImportacao.getValor().hashCode();
			} else {
				result = result + chaveImportacao.getValor().hashCode();
			}
			
		}
		return result;
	}

	@Override
	public int compareTo(final UnidadeImportacao outraUnidade) {
		int comparador = 0;
		for (CampoImportacao campo : this.campos) {
			for (CampoImportacao outroCampo : outraUnidade.getCampos()) {
				if (campo.getCampo().equals(outroCampo.getCampo())) {
					String valor = campo.getValor() != null ? campo.getValor() : "";
					String outroValor = outroCampo.getValor() != null ? outroCampo.getValor() : "";
					comparador = valor.compareTo(outroValor);
					break;
				}
			}
			if (comparador != 0) {
				return comparador;
			}
		}
		return comparador;
	}

	public void consolidar(final Map<Dominio, CampoPerfilConciliacao> camposConciliaveis, 
			final UnidadeImportacao unidadeConsolidar) {
		for (CampoImportacao campo : this.campos) {
			boolean campoConciliavel = camposConciliaveis.containsKey(campo.getCampo());
			if (campoConciliavel) {
				this.atualizaValorConsolidado(unidadeConsolidar, campo);
			}
		}
		
	}

	private void atualizaValorConsolidado(final UnidadeImportacao unidadeConsolidar,
			final CampoImportacao campo) {
		CampoImportacao campoAConsolidar = unidadeConsolidar.getCampoPorDominio(campo.getCampo());
		if (campo.getCampo().isConsolidated()) {
			BigDecimal valor1 = NumberUtil.obtemValor(campo.getValor(), campo.getPattern());
			BigDecimal valor2 = NumberUtil.obtemValor(campoAConsolidar.getValor(), campoAConsolidar.getPattern());
			campo.atualizaValorConsolidado(valor1.add(valor2).toPlainString());
		} else {
			if (!campo.getValor().equals(campoAConsolidar.getValor())) {
				campo.atualizaValorConsolidado(Constants.NAO_CONSOLIDADO);
			}
		}
	}
}