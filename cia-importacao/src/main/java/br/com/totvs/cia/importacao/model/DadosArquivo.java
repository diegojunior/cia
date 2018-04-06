package br.com.totvs.cia.importacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.infra.model.Model;

@Data
@Entity
@Table(name = "IM_DADOS_ARQUIVO")
@AllArgsConstructor
@NoArgsConstructor
public class DadosArquivo implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-dadosarquivo-uuid")
	@GenericGenerator(name = "system-dadosarquivo-uuid", strategy = "uuid")
	private String id;
	
	@Lob
	@Column(name = "dados")
	private byte[] dados;
	
}
