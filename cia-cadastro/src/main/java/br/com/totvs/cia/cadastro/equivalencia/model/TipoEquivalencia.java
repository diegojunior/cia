package br.com.totvs.cia.cadastro.equivalencia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.cadastro.equivalencia.json.TipoEquivalenciaJson;
import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CD_TIPO_EQUIVALENCIA")
@EqualsAndHashCode(callSuper = false, of = {"idLegado"})
public class TipoEquivalencia implements Model{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-tipoequivalencia-uuid")
	@GenericGenerator(name = "system-tipoequivalencia-uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "ID_LEGADO")
	private String idLegado;
	
	@Column(name = "CODIGO")
	private String codigo;
	
	public TipoEquivalencia(final TipoEquivalenciaJson json) {
		this.id = json.getId();
		this.idLegado = json.getIdLegado();
		this.codigo = json.getCodigo();
	}
}