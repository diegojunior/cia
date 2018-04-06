package br.com.totvs.cia.cadastro.carteira.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.cadastro.carteira.json.ClienteJson;
import br.com.totvs.cia.infra.model.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CD_CLIENTE")
@EqualsAndHashCode(callSuper = false, of = {"idLegado"})
public class Cliente implements Model{
	
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-cliente-uuid")
	@GenericGenerator(name = "system-cliente-uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "ID_LEGADO")
	private String idLegado;
	
	@Column(name = "CODIGO")
	private String codigo;
	
	@Column(name = "NOME")
	private String nome;	
	
	public Cliente(final String codigo) {
		this.codigo = codigo;
	}
	
	public Cliente(final ClienteJson json) {
		this.id = json.getId();
		this.idLegado = json.getIdLegado();
		this.codigo = json.getCodigo();
		this.nome = json.getNome();
	}
}