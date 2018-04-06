package br.com.totvs.cia.parametrizacao.unidade.importacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.collect.Lists;

import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.layout.json.CampoJson;
import br.com.totvs.cia.parametrizacao.unidade.importacao.model.ParametrizacaoUnidadeImportacaoBloco;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParametrizacaoUnidadeImportacaoBlocoResumidoJson implements Json {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "id")
	private String id;

	@JsonProperty(value = "codigo")
	private String codigo;
	
	@JsonProperty("campos")
	private List<CampoJson> campos;
	
	public List<CampoJson> getCampos() {
		if (this.campos == null) {
			return campos = Lists.newArrayList();
		}
		return this.campos;
	}
	
	public ParametrizacaoUnidadeImportacaoBlocoResumidoJson(final ParametrizacaoUnidadeImportacaoBloco model, final List<CampoJson> camposJson) {
		this.id = model.getId();
		this.codigo = model.getCodigo();
		this.campos = Lists.newArrayList(camposJson);
	}
}