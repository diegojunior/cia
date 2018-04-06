package br.com.totvs.cia.importacao.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.infra.model.Model;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "IM_ARQUIVO")
public class Arquivo implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-arquivo-uuid")
	@GenericGenerator(name = "system-arquivo-uuid", strategy = "uuid")
	private String id;
	
	private String fileName;
	
	private Long fileSize;
	
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private DadosArquivo dadosArquivo;
	
}
