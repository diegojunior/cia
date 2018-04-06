package br.com.totvs.cia.parametrizacao.unidade.importacao.model;

import java.util.List;

import javax.persistence.CascadeType;
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
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PA_CHAVE_UNIDADE_IMPORTACAO")
@EqualsAndHashCode(callSuper = false, of = { "id", "campo" })
public class ChaveParametrizacaoUnidadeImportacao implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-parametrizacaochaveunidade-uuid")
	@GenericGenerator(name = "system-parametrizacaochaveunidade-uuid", strategy = "uuid")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_CHAVE")
	private ParametrizacaoUnidadeImportacaoChave param;

	@ManyToOne
	@JoinColumn(name = "ID_CAMPO")
	private Campo campo;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "PA_CHAVE_UNIDADE_SESSAO",
		joinColumns = @JoinColumn(name = "ID_CHAVE_UNIDADE"), 
		inverseJoinColumns = @JoinColumn(name = "ID_SESSAO"))
	private List<Sessao> sessoes;
}
