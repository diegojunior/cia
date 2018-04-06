package br.com.totvs.cia.cadastro.grupo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.cadastro.grupo.json.GrupoJson;
import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CD_GRUPO")
@EqualsAndHashCode(callSuper = false, of = {"idLegado"})
public class Grupo implements Model {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-grupo-uuid")
	@GenericGenerator(name = "system-grupo-uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "ID_LEGADO")
	private String idLegado;
	
	@Column(name = "CODIGO")
	private String codigo;
	
	public Grupo(final GrupoJson json) {
		this.id = json.getId();
		this.idLegado = json.getIdLegado();
		this.codigo = json.getCodigo();
	}
}