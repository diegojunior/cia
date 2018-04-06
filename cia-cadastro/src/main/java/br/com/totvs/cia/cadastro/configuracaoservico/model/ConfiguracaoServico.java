package br.com.totvs.cia.cadastro.configuracaoservico.model;

import java.util.List;
import java.util.function.Consumer;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.base.model.SistemaEnum;
import br.com.totvs.cia.cadastro.configuracaoservico.json.ConfiguracaoServicoJson;
import br.com.totvs.cia.infra.model.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "CD_CONFIGURACAO_SERVICO")
@EqualsAndHashCode(callSuper = false, of = {"id"})
public class ConfiguracaoServico implements Model {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(generator = "system-configuracaoservico-uuid")
	@GenericGenerator(name = "system-configuracaoservico-uuid", strategy = "uuid")
	private String id;
	
	@Column(name = "CODIGO")
	private String codigo;
	
	@Column(name = "DESCRICAO")
	private String descricao;
	
	@Column(name = "SISTEMA")
	private SistemaEnum sistema;
	
	@Column(name = "SERVICO")
	private ServicoEnum servico;
	
	@Column(name = "TIPO_SERVICO")
	private TipoServicoEnum tipoServico;

	@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER, mappedBy = "configuracaoServico", orphanRemoval = true)
	private List<CampoConfiguracaoServico> campos;
	
	public ConfiguracaoServico(final ConfiguracaoServicoJson json, final List<CampoConfiguracaoServico> campos) {
		this.id = json.getId();
		this.codigo = json.getCodigo();
		this.descricao = json.getDescricao();
		this.sistema = SistemaEnum.fromCodigo(json.getSistema().getCodigo());
		this.servico = ServicoEnum.fromCodigo(json.getServico().getCodigo());
		this.tipoServico = TipoServicoEnum.fromCodigo(json.getTipoServico().getCodigo());
		this.campos = campos;
	}
	
	public ConfiguracaoServico(ConfiguracaoServicoJson json) {
		this.id = json.getId();
		this.codigo = json.getCodigo();
		this.descricao = json.getDescricao();
		this.sistema = SistemaEnum.fromCodigo(json.getSistema().getCodigo());
		this.servico = ServicoEnum.fromCodigo(json.getServico().getCodigo());
		this.tipoServico = TipoServicoEnum.fromCodigo(json.getTipoServico().getCodigo());
		this.campos = Lists.newArrayList();
	}

	public List<CampoConfiguracaoServico> getCampos() {
		if (this.campos == null) {
			this.campos = Lists.newArrayList();
		}
		return this.campos;
	}

	public CampoConfiguracaoServico getBy(String campo) {
		for (CampoConfiguracaoServico campoServico : this.campos) {
			if (campoServico.getCampo().equals(campo)) {
				return campoServico;
			}
		}
		return null;
	}
	
	public void atualizarCampos(final List<CampoConfiguracaoServico> campos) {
		for (CampoConfiguracaoServico campo : campos) {
			if (this.getCampos().contains(campo)) {
				this.getCampos().forEach(new Consumer<CampoConfiguracaoServico>() {
					@Override
					public void accept(CampoConfiguracaoServico outroCampo) {
						if (outroCampo.equals(campo)) {
							outroCampo.setLabel(campo.getLabel());
						}
					}
				});
			} else {
				this.getCampos().add(campo);
			}
		}
	}
	
	public Boolean isTipoPosicao(){
		return this.getTipoServico().isPosicao();
	}
	
	public Boolean isTipoMovimento(){
		return this.getTipoServico().isMovimento();
	}
	
	public Boolean isMovimentoRVFundoAMercado(){
		return this.getServico().isMovimentoRVFundoAMercado();
	}
	
	public Boolean isRendaVariavelAVista(){
		return this.getServico().isRendaVariavelAVista();
	}
	
	public Boolean isRendaVariavelOpcoes(){
		return this.getServico().isRendaVariavelOpcoes();
	}
	
	public Boolean isRendaVariavelEmprestimo(){
		return this.getServico().isRendaVariavelEmprestimo();
	}
	
	public Boolean isRendaVariavelTermo(){
		return this.getServico().isRendaVariavelTermo();
	}
	
	public Boolean isRendaVariavelFundoAMercado(){
		return this.getServico().isRendaVariavelFundoAMercado();
	}
	
	public Boolean isDerivativosDisponivel(){
		return this.getServico().isDerivativosDisponivel();
	}
	
	public Boolean isDerivativosFuturos(){
		return this.getServico().isDerivativosFuturos();
	}
	
	public Boolean isRendaFixaCompromissada(){
		return this.getServico().isRendaFixaCompromissada();
	}
	
	public Boolean isRendaFixaDefinitiva(){
		return this.getServico().isRendaFixaDefinitiva();
	}
	
	public Boolean isRendaFixaTermo(){
		return this.getServico().isRendaFixaTermo();
	}
	
	public Boolean isSwap(){
		return this.getServico().isSwap();
	}
	
	public Boolean isFundoInvestimento(){
		return this.getServico().isFundoInvestimento();
	}
	
	public Boolean isPatrimonio(){
		return this.getServico().isPatrimonio();
	}
}