package br.com.totvs.cia.cadastro.agente.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.cadastro.agente.json.AgenteJson;
import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CD_AGENTE")
public class Agente implements Model {
	
	private static final long serialVersionUID = 1L;
	
	public static final Agente AGENTE_NULL = null;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-agente-uuid")
	@GenericGenerator(name = "system-agente-uuid", strategy = "uuid")
	private String id;
	
	private String idLegado;

	private String codigo;
	
	private String descricao;
	
	private String codigoClearing;
	
	public Agente(final String idLegado, final String codigo, final String descricao,
			final String codigoClearing) {
		this.idLegado = idLegado;
		this.codigo = codigo;
		this.descricao = descricao;
		this.codigoClearing = codigoClearing;
	}

	public Agente(final Agente agente) {
		this.id = agente.getId();
		this.idLegado = agente.getIdLegado();
		this.codigo = agente.getCodigo();
		this.descricao = agente.getDescricao();
		this.codigoClearing = agente.getCodigoClearing();
	}

	public Agente(final AgenteJson json) {
		this.idLegado = json.getIdLegado();
		this.codigo = json.getCodigo();
		this.descricao = json.getDescricao();
		this.codigoClearing = json.getCodigoClearing();
	}
}