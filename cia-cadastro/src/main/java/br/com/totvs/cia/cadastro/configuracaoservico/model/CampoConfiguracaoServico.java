package br.com.totvs.cia.cadastro.configuracaoservico.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.cadastro.tipo.TipoValorDominioEnum;
import br.com.totvs.cia.infra.model.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "CD_CAMPO_CONFIG_SERVICO")
@EqualsAndHashCode(callSuper = false, of = "campo")
public class CampoConfiguracaoServico implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-campoconfigservico-uuid")
	@GenericGenerator(name = "system-campoconfigservico-uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "CAMPO")
	private String campo;
	
	@Column(name = "LABEL")
	private String label;
	
	@Column(name = "TIPO")
	private TipoValorDominioEnum tipo;
	
	@ManyToOne
	@JoinColumn(name = "ID_CONFIGURACAO_SERVICO")
	private ConfiguracaoServico configuracaoServico;
	
	public CampoConfiguracaoServico(final String campo, final String label) {
		this.campo = campo;
		this.label = label;
	}
	
	public CampoConfiguracaoServico(final CampoConfiguracaoServico campo, final ConfiguracaoServico configuracaoServico) {
		this.id = campo.getId();
		this.campo = campo.getCampo();
		this.label = campo.getLabel();
		this.configuracaoServico = configuracaoServico;
	}
}