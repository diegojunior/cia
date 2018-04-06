package br.com.totvs.cia.parametrizacao.unidade.importacao.model;

import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.google.common.collect.Lists;

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
@PrimaryKeyJoinColumn(name = "ID")
@Table(name = "PA_UNIDADE_IMPORTACAO_BLOCO")
public class ParametrizacaoUnidadeImportacaoBloco extends AbstractParametrizacaoUnidadeImportacao {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "ID_SESSAO_ABERTURA")
	private Sessao abertura;
	
	@ManyToOne
	@JoinColumn(name = "ID_SESSAO_FECHAMENTO")
	private Sessao fechamento;

	@OneToMany(cascade = CascadeType.ALL, mappedBy="param", orphanRemoval = true)
	private List<LinhaBlocoParametrizacaoUnidadeImportacao> linhas;
	
	public List<Campo> getCamposLayout() {
		final List<Campo> campos = Lists.newArrayList();
		for (LinhaBlocoParametrizacaoUnidadeImportacao linha : this.getLinhas()) {
			campos.addAll(linha.getCampos());
		}
		return campos;
	}
	
	public List<LinhaBlocoParametrizacaoUnidadeImportacao> getLinhas() {
		if (this.linhas == null) {
			this.linhas = Lists.newArrayList();
		}
		return this.linhas;
	}
	
	public void clearLinhas() {
		this.getLinhas().clear();
	}

	public void atualizaLinhas(final ParametrizacaoUnidadeImportacaoBloco param) {
		for (LinhaBlocoParametrizacaoUnidadeImportacao linha : this.getLinhas()) {
			linha.setParam(param);
		}
	}
	
	public LinhaBlocoParametrizacaoUnidadeImportacao getLinhaConfiguradaSessaoPosicao() {
		return this.getLinhas()
				.stream()
				.filter(linha -> !linha.getSessao().equals(ParametrizacaoUnidadeImportacaoBloco.this.abertura))
				.findAny()
				.get();
	}
	
	public LinhaBlocoParametrizacaoUnidadeImportacao getLinhaConfiguradaSessaoAbertura() {
		return this.getLinhas()
				.stream()
				.filter(linha -> linha.getSessao().equals(ParametrizacaoUnidadeImportacaoBloco.this.abertura))
				.findAny()
				.get();
	}

	@Override
	public Campo getCampoBy(final String codigoCampo) {
		for (LinhaBlocoParametrizacaoUnidadeImportacao linha : this.getLinhas()) {
			Optional<Campo> findAny = linha.getCampos()
				.stream()
				.filter(campo -> campo.getDominio().getCodigo().equals(codigoCampo))
				.findAny();
			if (findAny.isPresent()) {
				return findAny.get();
			}
		}
		return null;
	}
}