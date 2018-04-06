package br.com.totvs.cia.parametrizacao.dominio.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.cadastro.tipo.TipoValorDominioEnum;
import br.com.totvs.cia.cadastro.tipo.TipoValorDominioEnumDesirializer;
import br.com.totvs.cia.cadastro.tipo.TipoValorDominioEnumSerializer;
import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.dominio.json.DominioJson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PA_DOMINIO")
@EqualsAndHashCode(callSuper = false, of = {"id", "codigo"})
public class Dominio implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-dominio-uuid")
	@GenericGenerator(name = "system-dominio-uuid", strategy = "uuid")
	private String id;

	@Column(name = "CODIGO", length = 30)
	private String codigo;
	
	@Column(name = "TIPO")
	@JsonProperty("tipo")
	@JsonSerialize(using = TipoValorDominioEnumSerializer.class)
	@JsonDeserialize(using = TipoValorDominioEnumDesirializer.class)
	private TipoValorDominioEnum tipo;

	public Dominio(final DominioJson json) {
		this.id = json.getId();
		this.codigo = json.getCodigo();
		this.tipo = TipoValorDominioEnum.fromCodigo(json.getTipo().getCodigo());
	}
	
	public boolean isEfetuaCalculo() {
		return this.tipo.isEfetuaCalculo();
	}
	
	public boolean isConsolidated() {
		return isEfetuaCalculo();
	}
	
	public boolean isDate() {
		return tipo.isDate();
	}
	
}