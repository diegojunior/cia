package br.com.totvs.cia.parametrizacao.layout.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;

import lombok.Data;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "PA_LAYOUT", uniqueConstraints = {@UniqueConstraint(columnNames = {"CODIGO", "TIPO_LAYOUT"})})
public abstract class AbstractLayout implements Layout {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-layout-uuid")
	@GenericGenerator(name = "system-layout-uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "CODIGO", length = 30)
	private String codigo;
	
	private String descricao;
	
	@Column(name = "TIPO_LAYOUT")
	private TipoLayoutEnum tipoLayout;
	
	@Column(name = "STATUS_LAYOUT")
	private StatusLayoutEnum status;

	@Override
	public Campo getCampoSessaoBy(final String codigoSessao, final String codigoDominio) {
		for (Sessao sessao : this.getSessoes()) {
			if(sessao.getCodigo().equalsIgnoreCase(codigoSessao)) {
				for (Campo campo : sessao.getCampos()) {
					if(campo.getDominio().getCodigo().equalsIgnoreCase(codigoDominio)) {
						return campo;
					}
				}
			}
		}
		return null;
	}
	
	@Override
	public Sessao getBy(final String codigoSessao) {
		for (Sessao sessao : this.getSessoes()) {
			if(sessao.getCodigo().equalsIgnoreCase(codigoSessao)) {
				return sessao;
			}
		}
		return null;
	}
	
	@Override
	public TipoDelimitadorEnum getTipoDelimitador() {
		return null;
	}
	
}
