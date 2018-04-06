package br.com.totvs.cia.parametrizacao.validacao.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import br.com.totvs.cia.parametrizacao.layout.model.Layout;
import br.com.totvs.cia.parametrizacao.layout.model.TipoLayoutEnum;
import br.com.totvs.cia.parametrizacao.validacao.json.ValidacaoArquivoJson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "ID")
@Table(name = "PA_VALIDACAO_ARQUIVO_EXT")
public class ValidacaoArquivoExterno extends AbstractValidacaoArquivo {

	private static final long serialVersionUID = 1L;

	@Column(name="POSICAO_INICIAL")
	private Integer posicaoInicial;

	@Column(name="POSICAO_FINAL")
	private Integer posicaoFinal;
	
	@Column(name = "PATTERN")
	private String pattern;
	
	public ValidacaoArquivoExterno(final ValidacaoArquivoJson json, final Layout layout) {
		super(json.getId(),
			  TipoValidacaoEnum.ARQUIVO,
		      TipoLayoutEnum.fromCodigo(json.getTipoLayout().getCodigo()),
		      layout,
		      LocalValidacaoArquivoEnum.fromCodigo(json.getLocalValidacao().getCodigo()),
		      CampoValidacaoArquivoEnum.fromCodigo(json.getCampoValidacao().getCodigo()));
		
		this.posicaoInicial = json.getPosicaoInicial();
		this.posicaoFinal = json.getPosicaoFinal();
		this.pattern = json.getFormato();
	}
	
}