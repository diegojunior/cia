package br.com.totvs.cia.parametrizacao.validacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "ID")
@Table(name = "PA_VALIDACAO_ARQUIVO")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AbstractValidacaoArquivo extends AbstractValidacao {

	private static final long serialVersionUID = 1L;
	
	@Column(name="LOCAL_VALIDACAO")
	private LocalValidacaoArquivoEnum localValidacao;
	
	@Column(name="CAMPO_VALIDACAO")
	private CampoValidacaoArquivoEnum campoValidacao;
	
	public AbstractValidacaoArquivo(final String id, final TipoValidacaoEnum tipoValidacao, 
			final TipoLayoutEnum tipoLayout, final Layout layout, final LocalValidacaoArquivoEnum localValidacao, 
			final CampoValidacaoArquivoEnum campoValidacao) {
		super(id, tipoValidacao, tipoLayout, layout);
		this.localValidacao = localValidacao;
		this.campoValidacao = campoValidacao;
	}
	
}