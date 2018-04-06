package br.com.totvs.cia.parametrizacao.unidade.importacao.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PA_LINHA_BLOCO_UND_IMP")
public class LinhaBlocoParametrizacaoUnidadeImportacao implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-parametrizacaoblocounidadelinha-uuid")
	@GenericGenerator(name = "system-parametrizacaoblocounidadelinha-uuid", strategy = "uuid")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_BLOCO")
	private ParametrizacaoUnidadeImportacaoBloco param;

	@ManyToOne
	@JoinColumn(name = "ID_SESSAO")
	private Sessao sessao;

	@ManyToMany
	@JoinTable(name = "PA_LINHA_BLOCO_CAMPO", joinColumns = @JoinColumn(name = "ID_LINHA_BLOCO"), inverseJoinColumns = @JoinColumn(name = "ID_CAMPO"))
	private List<Campo> campos;
}