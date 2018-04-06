package br.com.totvs.cia.cadastro.equivalencia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.cadastro.equivalencia.json.EquivalenciaJson;
import br.com.totvs.cia.infra.model.Model;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "CD_EQUIVALENCIA")
@Data
@NoArgsConstructor
public class Equivalencia implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-equivalencia-uuid")
	@GenericGenerator(name = "system-equivalencia-uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "ID_LEGADO")
	private String idLegado;

	@Column(name = "CODIGO_INTERNO")
	private String codigoInterno;
	
	@Column(name = "CODIGO_EXTERNO")
	private String codigoExterno;
	
	public Equivalencia(final EquivalenciaJson json) {
		this.id = json.getId();
		this.idLegado = json.getIdLegado();
		this.codigoInterno = json.getCodigoInterno();
		this.codigoExterno = json.getCodigoExterno();
	}

	
}
