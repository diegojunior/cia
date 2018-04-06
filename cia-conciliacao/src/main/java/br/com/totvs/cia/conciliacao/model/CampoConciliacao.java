package br.com.totvs.cia.conciliacao.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import br.com.totvs.cia.cadastro.configuracaoservico.model.CampoConfiguracaoServico;
import br.com.totvs.cia.carga.model.CampoUnidadeCarga;
import br.com.totvs.cia.importacao.model.CampoImportacao;
import br.com.totvs.cia.infra.model.Model;
import br.com.totvs.cia.infra.util.Constants;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.infra.util.NumberUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CO_CAMPO_CONCILIACAO")
@ToString(callSuper = false, of = {"id"})
public class CampoConciliacao implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-campoconciliacao-uuid")
	@GenericGenerator(name = "system-campoconciliacao-uuid", strategy = "uuid")
	private String id;

	@Column(name = "CAMPO")
	private String campo;
	
	@Column(name = "VALOR_CARGA")
	private String valorCarga;
	
	@Column(name = "VALOR_IMPORTACAO")
	private String valorImportacao;
	
	@Column(name = "VALOR_CONCILIACAO")
	private String valorConciliacao;
	
	@Embedded
	private CampoEquivalenteConciliacao campoEquivalenteConciliacao;
	
	@Column(name = "STATUS", nullable = false)
	private StatusCampoConciliacaoEnum status;
	
	@ManyToOne
	@JoinColumn(name = "ID_UNIDADE_CONCILIACAO", nullable = false)
	private UnidadeConciliacao unidade;
	
	public static CampoConciliacao buildCampoConciliacao(final String label, final CampoUnidadeCarga campoCarga,
			final CampoImportacao campoImportacao,
			final UnidadeConciliacao unidadeConciliacao) {
		final CampoConciliacao campo = new CampoConciliacao();
		String valorCarga = campoCarga.getValor();
		String valorImportacao = campoImportacao.getValor();
		StatusCampoConciliacaoEnum status = StatusCampoConciliacaoEnum.OK;
		String valorConciliacao = StatusCampoConciliacaoEnum.OK.getNome();
		try {
			if (Constants.NAO_CONSOLIDADO.equals(valorCarga)
					|| Constants.NAO_CONSOLIDADO.equals(valorImportacao)) {
				valorConciliacao = StatusCampoConciliacaoEnum.DIVERGENTE.getNome();
				status = StatusCampoConciliacaoEnum.DIVERGENTE;
			} else if (campoImportacao.getCampo().isEfetuaCalculo()) {
				final BigDecimal valorCargaConvertido = new BigDecimal(valorCarga.replace(",", "."));
				final BigDecimal valorImportacaoConvertido = NumberUtil.obtemValor(valorImportacao, campoImportacao.getPattern());
				
				final BigDecimal resultado = FormatadorConciliacao.subtrair(valorCargaConvertido, valorImportacaoConvertido);
				if (resultado.doubleValue() < 0
						|| resultado.doubleValue() > 0) {
					valorConciliacao = resultado.toPlainString();
					status = StatusCampoConciliacaoEnum.DIVERGENTE;
				}
				valorCarga = valorCargaConvertido.toString();
				valorImportacao = valorImportacaoConvertido.toString();
				
			} else if (campoImportacao.getCampo().getTipo().isDate()) {
				Date dataCarga = DateUtil.parse(valorCarga, DateUtil.yyyyMMdd);
				Date dataImportacao = DateUtil.parse(valorImportacao, campoImportacao.getPattern());
				
				status = FormatadorConciliacao.datasIguais(dataCarga, dataImportacao)
							? StatusCampoConciliacaoEnum.OK 
							: StatusCampoConciliacaoEnum.DIVERGENTE;
				valorConciliacao = status.getNome();
				valorCarga = DateUtil.format(dataCarga, DateUtil.yyyyMMdd);
				valorImportacao = DateUtil.format(dataImportacao, DateUtil.yyyyMMdd);
				
			} else if (!valorCarga.equals(valorImportacao)) {
				status = StatusCampoConciliacaoEnum.DIVERGENTE;
				valorConciliacao = status.getNome();
			}
			
		} catch (IllegalArgumentException | ParseException e) {
			throw new IllegalArgumentException("O valor informado na Carga / Importação possui Formato diferente para o campo " + "\"" + label + "\"");
		}
				
		campo
			.comCampo(label)
			.comStatus(status)
			.comUnidadeConciliacao(unidadeConciliacao)
			.comValorCarga(valorCarga)
			.comValorImportacao(valorImportacao)
			.atualizaValor(valorConciliacao);
		
		return campo;
	}
	

	public static CampoConciliacao buildCampoConciliacao(final String label, final CampoUnidadeCarga campoCarga,
			final UnidadeConciliacao unidadeConciliacao) {
		final CampoConciliacao campoConciliacao = new CampoConciliacao();
		final String valorCarga = campoCarga.getValor();
		campoConciliacao
			.comCampo(label)
			.comStatus(StatusCampoConciliacaoEnum.SOMENTE_CARGA)
			.comUnidadeConciliacao(unidadeConciliacao)
			.comValorCarga(valorCarga)
			.comValorImportacao(null)
			.atualizaValor(StatusCampoConciliacaoEnum.SOMENTE_CARGA.getNome());
		
		return campoConciliacao;
	}
	
	public static CampoConciliacao buildCampoConciliacao(final CampoImportacao campoImportacao,
			final UnidadeConciliacao unidadeConciliacao) {
		
		
		final CampoConciliacao campoConciliacao = new CampoConciliacao();
		
		final String valorImportacao = FormatadorConciliacao.format(campoImportacao);
		
		campoConciliacao
			.comCampo(campoImportacao.getCampo().getCodigo())
			.comStatus(StatusCampoConciliacaoEnum.SOMENTE_IMPORTACAO)
			.comUnidadeConciliacao(unidadeConciliacao)
			.comValorCarga(null)
			.comValorImportacao(valorImportacao)
			.atualizaValor(StatusCampoConciliacaoEnum.SOMENTE_IMPORTACAO.getNome());
		
		return campoConciliacao;
	}

	public String getValor() {
		return this.valorConciliacao;
	}

	public void atualizaValor(final String novoValor) {
		this.valorConciliacao = novoValor;
	}

	public CampoConciliacao comValorCarga(final String valorCarga) {
		this.valorCarga = valorCarga;
		return this;
	}

	public CampoConciliacao comValorImportacao(final String valorImportacao) {
		this.valorImportacao = valorImportacao;
		return this;
	}

	public CampoConciliacao comUnidadeConciliacao(
			final UnidadeConciliacao unidadeConciliacao) {
		this.unidade = unidadeConciliacao;
		this.unidade.getCamposConciliaveis().add(this);
		return this;
	}

	public CampoConciliacao comStatus(
			final StatusCampoConciliacaoEnum status) {
		this.status = status;
		return this;
	}

	public void comCampoEquivalente(final CampoConfiguracaoServico campoEquivalente,
			final CampoUnidadeCarga campoCargaEquivalente) {
		this.campoEquivalenteConciliacao = new CampoEquivalenteConciliacao(campoEquivalente, campoCargaEquivalente);
		
	}

	public void comCampoEquivalente(final CampoConfiguracaoServico campoEquivalente) {
		this.campoEquivalenteConciliacao = new CampoEquivalenteConciliacao(campoEquivalente);
		
	}
	
	public CampoConciliacao comCampo(final String codigo) {
		this.campo = codigo;
		return this;
	}
}