package br.com.totvs.cia.parametrizacao.perfilconciliacao.model;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.configuracaoservico.model.ConfiguracaoServico;
import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.AbstractParametrizacaoUnidadeImportacao;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "PA_CONFIGURACAO_PERFIL")
public class ConfiguracaoPerfilConciliacao implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-configuracaoperfil-uuid")
	@GenericGenerator(name = "system-configuracaoperfil-uuid", strategy = "uuid")
	private String id;
	
	@OneToOne
	@JoinColumn(name = "ID_PERFIL_CONCILIACAO")
	private PerfilConciliacao perfil;
	
	@ManyToOne
	@JoinColumn(name = "ID_SESSAO_LAYOUT")
	private Sessao sessao;
	
	@ManyToOne
	@JoinColumn(name = "ID_PARAMETRIZACAO_UNIDADE")
	private AbstractParametrizacaoUnidadeImportacao unidade;
	
	@ManyToOne
	@JoinColumn(name = "ID_CONFIGURACAO_SERVICO")
	private ConfiguracaoServico servico;
	
	@Column(name = "CONSOLIDAR_DADOS")
	private boolean consolidarDados;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "configuracaoPerfil")
	private List<CampoPerfilConciliacao> campos;
	
	public ConfiguracaoPerfilConciliacao(final ConfiguracaoPerfilConciliacao configuracao,
			final PerfilConciliacao perfilConciliacao) {
		this.id = configuracao.getId();
		this.perfil = perfilConciliacao;
		this.sessao = configuracao.getSessao();
		this.unidade = configuracao.getUnidade();
		this.servico = configuracao.getServico();
		this.consolidarDados = configuracao.isConsolidarDados();
		this.campos = new CamposPerfil(configuracao.getCampos(), this).getCampos();
	}
	
	public ConfiguracaoPerfilConciliacao(final Sessao sessao, 
			final ConfiguracaoServico servico, final List<CampoPerfilConciliacao> campos, boolean consolidarDados) {
		this.sessao = sessao;
		this.servico = servico;
		this.campos = new CamposPerfil(campos, this).getCampos();
		this.consolidarDados = consolidarDados;
	}
	
	public ConfiguracaoPerfilConciliacao(final AbstractParametrizacaoUnidadeImportacao unidade, 
			final ConfiguracaoServico servico, final List<CampoPerfilConciliacao> campos, boolean consolidarDados) {
		this.unidade = unidade;
		this.servico = servico;
		this.campos = new CamposPerfil(campos, this).getCampos();
		this.consolidarDados = consolidarDados;
	}
	
	public List<CampoPerfilConciliacao> getCampos() {
		if (this.campos == null) {
			this.campos = Lists.newArrayList();
		}
		return this.campos;
	}
	
	public void setConfiguracoes(final List<CampoPerfilConciliacao> campos) {
		if (this.campos == null) {
			this.campos = Lists.newArrayList(campos);
		} else {
			this.campos.clear();
			this.campos.addAll(campos);
		}
	}

	public List<CampoPerfilConciliacao> getChaves() {
		return this.getCampos()
					.stream()
					.filter(campo -> campo.getIsChave())
					.collect(Collectors.toList());
	}

	public List<CampoPerfilConciliacao> getCamposConciliaveis() {
		return this.getCampos()
					.stream()
					.filter(campo -> campo.getIsConciliavel())
					.collect(Collectors.toList());
	}

	public List<CampoPerfilConciliacao> getCamposInformativos() {
		return this.getCampos().stream().filter(campo -> campo.getIsInformativo()).collect(Collectors.toList());
	}
	
	public Campo getCampoBy(final String campo) {
		if (this.sessao != null) {
			return this.sessao.getCampoBy(campo);
		}
		return this.unidade.getCampoBy(campo);
	}
}