package br.com.totvs.cia.parametrizacao.perfilconciliacao.model;

import java.util.Iterator;
import java.util.List;

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

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.layout.model.AbstractLayout;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.PerfilConciliacaoJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PA_PERFIL_CONCILIACAO")
@EqualsAndHashCode(callSuper = false, of = {"id"})
public class PerfilConciliacao implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-perfil-uuid")
	@GenericGenerator(name = "system-perfil-uuid", strategy = "uuid")
	private String id;

	@Column(name = "CODIGO", unique = true, length = 30)
	private String codigo;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "SISTEMA")
	private SistemaEnum sistema;
	
	@ManyToOne(targetEntity = AbstractLayout.class)
	@JoinColumn(name = "ID_LAYOUT")
	private Layout layout;
	
	@Column(name = "STATUS")
	private StatusPerfilEnum status;
	
	@OneToOne(cascade=CascadeType.ALL, mappedBy="perfil", optional = false)
	private ConfiguracaoPerfilConciliacao configuracao;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "perfil", orphanRemoval = true)
	private List<Regra> regras;
	
	public PerfilConciliacao(final PerfilConciliacaoJson json, final Layout layout, final ConfiguracaoPerfilConciliacao configuracao, 
			final List<Regra> regras) {
		this.codigo = json.getIdentificacao().getCodigo();
		this.descricao = json.getIdentificacao().getDescricao();
		this.sistema = SistemaEnum.fromCodigo(json.getIdentificacao().getSistema().getCodigo());
		this.layout = layout;
		this.status = StatusPerfilEnum.ATIVO;
		this.configuracao = new ConfiguracaoPerfilConciliacao(configuracao, this);
		this.regras = new Regras(regras, this).getRegras();
	}
	
	public List<Regra> getRegras () {
		if (this.regras == null) {
			this.regras = Lists.newArrayList();
		}
		return this.regras;
	}
	
	public List<Regra> listRegrasCarga () {
		Iterator<Regra> regrasFiltradas = this.regras.stream().filter(modulo -> modulo.getModulo().isCarga()).iterator();
		if (regrasFiltradas != null && regrasFiltradas.hasNext()) {
			return Lists.newArrayList(regrasFiltradas);
		}
		return Lists.newArrayList();
	}
	
	public List<Regra> listRegrasImportacao () {
		Iterator<Regra> regrasFiltradas = this.regras.stream().filter(modulo -> modulo.getModulo().isImportacao()).iterator();
		if (regrasFiltradas != null && regrasFiltradas.hasNext()) {
			return Lists.newArrayList(regrasFiltradas);
		}
		return Lists.newArrayList();
	}

	public boolean consolidarDados() {
		return this.configuracao.isConsolidarDados();
	}
}