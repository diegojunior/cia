package br.com.totvs.cia.parametrizacao.perfilconciliacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnum;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumDeserializer;
import br.com.totvs.cia.cadastro.base.json.SistemaJsonEnumSerializer;
import br.com.totvs.cia.infra.json.Json;
import br.com.totvs.cia.parametrizacao.layout.json.LayoutJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJson;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJsonDeserializer;
import br.com.totvs.cia.parametrizacao.layout.json.TipoLayoutEnumJsonSerializer;
import br.com.totvs.cia.parametrizacao.perfilconciliacao.model.PerfilConciliacao;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdentificacaoJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	private String codigo;
	
	private String descricao;
	
	@JsonProperty("sistema")
	@JsonDeserialize(using = SistemaJsonEnumDeserializer.class)
	@JsonSerialize(using = SistemaJsonEnumSerializer.class)
	private SistemaJsonEnum sistema;
	
	@JsonProperty("status")
	@JsonDeserialize(using = StatusPerfilEnumJsonDeserializer.class)
	@JsonSerialize(using = StatusPerfilEnumJsonSerializer.class)
	private StatusPerfilJsonEnum status;
	
	@JsonProperty("tipoLayout")
	@JsonDeserialize(using = TipoLayoutEnumJsonDeserializer.class)
	@JsonSerialize(using = TipoLayoutEnumJsonSerializer.class)
	private TipoLayoutEnumJson tipoLayout;
	
	private LayoutJson layout;
	
	public IdentificacaoJson(final PerfilConciliacao model, final LayoutJson layout) {
		this.codigo = model.getCodigo();
		this.descricao = model.getDescricao();
		this.sistema = SistemaJsonEnum.fromCodigo(model.getSistema().getCodigo());
		this.status = StatusPerfilJsonEnum.fromCodigo(model.getStatus().getCodigo());
		this.tipoLayout = layout.getTipoLayout();
		this.layout = layout;
	}
}