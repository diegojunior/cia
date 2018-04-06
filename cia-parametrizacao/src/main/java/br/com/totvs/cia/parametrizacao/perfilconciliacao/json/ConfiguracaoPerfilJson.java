package br.com.totvs.cia.parametrizacao.perfilconciliacao.json;

import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.ConfiguracaoPerfilConciliacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracaoPerfilJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	@JsonProperty("localizacaoCampos")
	@JsonDeserialize(using = LocalizacaoCamposJsonEnumDeserializer.class)
	@JsonSerialize(using = LocalizacaoCamposJsonEnumSerializer.class)
	private LocalizacaoCamposJsonEnum localizacaoCampos;
	
	private String identificacao;
	
	private String servico;
	
	private boolean consolidarDados;
	
	private List<ConfiguracaoCampoJson> configuracoesCampos = Lists.newArrayList();
	
	public ConfiguracaoPerfilJson(final ConfiguracaoPerfilConciliacao model, final List<ConfiguracaoCampoJson> configuracoesCampos) {
		this.servico = model.getServico().getCodigo();
		this.configuracoesCampos = Lists.newArrayList(configuracoesCampos);
		if (model.getUnidade() != null) {
			this.localizacaoCampos = LocalizacaoCamposJsonEnum.UNIDADE;
			this.identificacao = model.getUnidade().getCodigo();
		}
		if (model.getSessao() != null) {
			this.localizacaoCampos = LocalizacaoCamposJsonEnum.SESSAO;
			this.identificacao = model.getSessao().getNome();
		}
		this.consolidarDados = model.isConsolidarDados();
	}
	
	public List<ConfiguracaoCampoJson> getConfiguracoesCampos() {
		
		List<ConfiguracaoCampoJson> chaves = this.configuracoesCampos
			.stream()
			.filter(item -> item.getChave())
			.collect(Collectors.toList());
		
		List<ConfiguracaoCampoJson> conciliaveis = this.configuracoesCampos
			.stream()
			.filter(item -> item.getConciliavel())
			.collect(Collectors.toList());
		
		List<ConfiguracaoCampoJson> informativos = this.configuracoesCampos
			.stream()
			.filter(item -> item.getInformativo())
			.collect(Collectors.toList());
		
		this.configuracoesCampos.clear();
		this.configuracoesCampos.addAll(chaves);
		this.configuracoesCampos.addAll(conciliaveis);
		this.configuracoesCampos.addAll(informativos);
		
		return this.configuracoesCampos;
	}
}