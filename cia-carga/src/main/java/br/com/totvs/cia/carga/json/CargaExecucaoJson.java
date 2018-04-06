package br.com.totvs.cia.carga.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Lists;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumDeserializer;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumSerializer;
import br.com.totvs.cia.cadastro.carteira.json.ClienteJson;
import br.com.totvs.cia.cadastro.configuracaoservico.json.ConfiguracaoServicoJson;
import br.com.totvs.cia.cadastro.grupo.json.GrupoJson;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CargaExecucaoJson implements Json {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String data;

	@JsonProperty("sistema")
	@JsonSerialize(using = SistemaJsonEnumSerializer.class)
	@JsonDeserialize(using = SistemaJsonEnumDeserializer.class)
	private SistemaJsonEnum sistema;
	
	private List<ConfiguracaoServicoJson> servicos;
	
	@JsonProperty("tipoExecucao")
	@JsonSerialize(using = TipoExecucaoCargaJsonEnumSerializer.class)
	@JsonDeserialize(using = TipoExecucaoCargaJsonEnumDeserializer.class)
	private TipoExecucaoCargaJsonEnum tipoExecucao;

	private List<ClienteJson> clientes;
	
	private List<GrupoJson> grupos;
	
	public List<ConfiguracaoServicoJson> getServicos () {
		if (this.servicos == null) {
			return Lists.newArrayList();
		}
		return this.servicos;
	}
	
	public List<ClienteJson> getClientes () {
		if (this.clientes == null) {
			return Lists.newArrayList();
		}
		return this.clientes;
	}
	
	public List<GrupoJson> getGrupos () {
		if (this.grupos == null) {
			return Lists.newArrayList();
		}
		return this.grupos;
	}
}