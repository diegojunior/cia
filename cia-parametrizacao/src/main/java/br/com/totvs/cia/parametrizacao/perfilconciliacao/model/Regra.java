package br.com.totvs.cia.parametrizacao.perfilconciliacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.cadastro.base.model.CondicaoEnum;
import br.com.totvs.cia.cadastro.base.model.ModuloEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.RegraPerfilJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PA_REGRA_PERFIL")
public class Regra implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-regra-uuid")
	@GenericGenerator(name = "system-regra-uuid", strategy = "uuid")
	private String id;

	@ManyToOne
	@JoinColumn(name="ID_PERFIL")
	private PerfilConciliacao perfil;
	
	@Column(name = "MODULO")
	private ModuloEnum modulo;
	
	@ManyToOne
	@JoinColumn(name="ID_CAMPO_CARGA")
	private CampoConfiguracaoServico campoCarga;
	
	@ManyToOne
	@JoinColumn(name="ID_CAMPO_IMPORTACAO")
	private Campo campoImportacao;
	
	@Column(name = "CONDICAO")
	private CondicaoEnum condicao;
	
	@Column(name = "FILTRO")
	private String filtro;
	
	public Regra(final RegraPerfilJson json, final CampoConfiguracaoServico campoCarga, final Campo campoImportacao) {
		this.modulo = ModuloEnum.fromCodigo(json.getModulo().getCodigo());
		this.condicao = CondicaoEnum.fromCodigo(json.getCondicao().getCodigo());
		this.filtro = json.getFiltro();
		this.campoCarga = campoCarga;
		this.campoImportacao = campoImportacao;
	}
}
