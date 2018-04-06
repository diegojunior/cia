package br.com.totvs.cia.conciliacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.CampoPerfilConciliacao;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CO_CAMPO_INFORMATIVO")
@ToString(callSuper = false, of = {"id"})
public class CampoInformativo implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-campoinformativo-uuid")
	@GenericGenerator(name = "system-campoinformativo-uuid", strategy = "uuid")
	private String id;

	@Column(name = "CAMPO")
	private String campo;
	
	@Column(name = "VALOR")
	private String valor;
	
	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_CONCILIACAO", nullable = false)
	private UnidadeConciliacao unidade;
	
	private CampoInformativo(final String campo, final String valor, final UnidadeConciliacao unidade) {
		this.campo = campo;
		this.valor = valor;
		this.unidade = unidade;
		this.unidade.getCamposInformativos().add(this);
	}
	
	public static CampoInformativo buildCampo(final CampoPerfilConciliacao campo, final String valor, final UnidadeConciliacao unidadeConciliacao) {
		return new CampoInformativo(campo.getCampoCarga().getLabel(), valor, unidadeConciliacao);
	}

}