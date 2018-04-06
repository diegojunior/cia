package br.com.totvs.cia.carga.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.parametrizacao.dominio.model.Dominio;
import br.com.totvs.cia.parametrizacao.infra.Campo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CA_CAMPO_UNIDADE_CARGA")
@ToString(callSuper = false, of = { "campoServico", "valor" })
public class CampoUnidadeCarga implements Campo {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-campounidadecarga-uuid")
	@GenericGenerator(name = "system-campounidadecarga-uuid", strategy = "uuid")
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_CARGA", nullable = false)
	private UnidadeCarga unidade;

	@ManyToOne
	@JoinColumn(name = "ID_CAMPO_CONFIGURACAO_SERVICO")
	private CampoConfiguracaoServico campoServico;

	@Column(name = "VALOR")
	private String valor;
	
	private transient String valorConsolidado;

	public CampoUnidadeCarga(final CampoConfiguracaoServico campo, final String valor) {
		this.campoServico = campo;
		this.valor = valor;
	}

	public CampoUnidadeCarga(final CampoConfiguracaoServico campo, final UnidadeCarga unidade, final String valor) {
		this.campoServico = campo;
		this.valor = valor;
		this.unidade = unidade;
	}

	@Override
	public void atualizaValor(final String novoValor) {
		this.valor = novoValor;
	}

	@Override
	public String getValor() {
		if (this.valorConsolidado != null) 
			return this.valorConsolidado;
		return this.valor;
	}

	@Override
	public Dominio getCampo() {
		throw new RuntimeException("Implementação não utilizada.");
	}
	
	public boolean isEfetuaCalculo() {
		return this.campoServico.getTipo().isEfetuaCalculo();
	}
	
	public boolean isConsolidated() {
		return isEfetuaCalculo();
	}

	public void atualizaValorConsolidado(final String valorConsolidado) {
		this.valorConsolidado = valorConsolidado;
		
	}
}