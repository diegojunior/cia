package br.com.totvs.cia.parametrizacao.validacao.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.com.totvs.cia.parametrizacao.layout.model.Campo;
import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.Sessao;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.validacao.json.ValidacaoArquivoJson;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "ID")
@Table(name = "PA_VALIDACAO_ARQUIVO_INT")
@EqualsAndHashCode(callSuper = false)
public class ValidacaoArquivoInterno extends AbstractValidacaoArquivo {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "ID_SESSAO_LAYOUT")
	private Sessao sessaoLayout;
	
	@ManyToOne
	@JoinColumn(name = "ID_CAMPO_LAYOUT")
	private Campo campoLayout;

	public ValidacaoArquivoInterno(final ValidacaoArquivoJson json, final Layout layout, 
			final Sessao sessao, final Campo campo) {
		super(json.getId(),
			  TipoValidacaoEnum.ARQUIVO,
		      TipoLayoutEnum.fromCodigo(json.getTipoLayout().getCodigo()),
		      layout,
		      LocalValidacaoArquivoEnum.fromCodigo(json.getLocalValidacao().getCodigo()),
		      CampoValidacaoArquivoEnum.fromCodigo(json.getCampoValidacao().getCodigo()));
		
		this.sessaoLayout = sessao;
		this.campoLayout = campo;
	}

}