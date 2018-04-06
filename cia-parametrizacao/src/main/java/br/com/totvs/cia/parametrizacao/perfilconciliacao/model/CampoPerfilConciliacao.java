package br.com.totvs.cia.parametrizacao.perfilconciliacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.json.ConfiguracaoCampoJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PA_CAMPO_PERFIL")
public class CampoPerfilConciliacao implements Model, Comparable<CampoPerfilConciliacao> {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-campoperfil-uuid")
	@GenericGenerator(name = "system-campoperfil-uuid", strategy = "uuid")
	private String id;
	
	@Setter
	@ManyToOne
	@JoinColumn(name="ID_CONFIGURACAO_PERFIL")
	private ConfiguracaoPerfilConciliacao configuracaoPerfil;
	
	@ManyToOne
	@JoinColumn(name="ID_CAMPO_IMPORTACAO")
	private Dominio campoImportacao;
	
	@ManyToOne
	@JoinColumn(name="ID_CAMPO_CARGA")
	private CampoConfiguracaoServico campoCarga;
	
	@ManyToOne
	@JoinColumn(name="ID_CAMPO_EQUIVALENTE")
	private CampoConfiguracaoServico campoEquivalente;
	
	@Column(name="CHAVE")
	private Boolean isChave;
	
	@Column(name="CONCILIAVEL")
	private Boolean isConciliavel;
	
	@Column(name="INFORMATIVO")
	private Boolean isInformativo;
	
	@Column(name="ORDEM")
	private Integer ordem;
	
	public CampoPerfilConciliacao(final ConfiguracaoCampoJson json, final CampoConfiguracaoServico campoCarga,
			final Dominio campoImportacao, final CampoConfiguracaoServico campoEquivalente) {
		this.isChave = json.getChave();
		this.isConciliavel = json.getConciliavel();
		this.isInformativo = json.getInformativo();
		this.campoCarga = campoCarga;
		this.campoImportacao = campoImportacao;
		this.campoEquivalente = campoEquivalente;
		this.ordem = json.getOrdem();
	}

	public CampoPerfilConciliacao(final CampoPerfilConciliacao campo, final ConfiguracaoPerfilConciliacao configuracao) {
		this.isChave = campo.getIsChave();
		this.isConciliavel = campo.getIsConciliavel();
		this.isInformativo = campo.getIsInformativo();
		this.campoCarga = campo.getCampoCarga();
		this.campoImportacao = campo.getCampoImportacao();
		this.campoEquivalente = campo.getCampoEquivalente();
		this.configuracaoPerfil = configuracao;
		this.ordem = campo.getOrdem();
	}

	@Override
	public int compareTo(final CampoPerfilConciliacao outro) {
		return this.getOrdem().compareTo(outro.getOrdem());
	}
}