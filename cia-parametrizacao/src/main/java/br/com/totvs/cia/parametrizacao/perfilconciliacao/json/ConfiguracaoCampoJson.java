package br.com.totvs.cia.parametrizacao.perfilconciliacao.json;

import br.com.totvs.cia.cadastro.configuracaoservico.json.CampoConfiguracaoServicoJson;
import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.CampoPerfilConciliacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracaoCampoJson implements Json, Comparable<ConfiguracaoCampoJson> {
	
	private static final long serialVersionUID = 1L;

	private Boolean chave;
	
	private Boolean conciliavel;
	
	private Boolean informativo;
	
	private CampoConfiguracaoServicoJson campoCarga;
	
	private String campoImportacao;
	
	private CampoConfiguracaoServicoJson campoEquivalente;
	
	private Integer ordem;
	
	public ConfiguracaoCampoJson(final CampoPerfilConciliacao model, final CampoConfiguracaoServicoJson campoCargaJson, 
			final CampoConfiguracaoServicoJson campoEquivalenteJson) {
		this.chave = model.getIsChave();
		this.conciliavel = model.getIsConciliavel();
		this.informativo = model.getIsInformativo();
		this.campoCarga = campoCargaJson;
		this.campoImportacao = model.getCampoImportacao() != null ? model.getCampoImportacao().getCodigo() : "";
		this.campoEquivalente = campoEquivalenteJson;
		this.ordem = model.getOrdem();
	}

	@Override
	public int compareTo(ConfiguracaoCampoJson outro) {
		return this.ordem.compareTo(outro.getOrdem());
	}
}