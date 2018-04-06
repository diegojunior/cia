package br.com.totvs.cia.conciliacao.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.conciliacao.model.CampoConciliacao;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampoConciliacaoJson implements Json {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String nome;

	private String valorCarga;
	
	private String valorImportacao;
	
	private String valorConciliacao;
	
	private String campoEquivalente;
	
	private String valorEquivalente;
	
	@JsonProperty("status")
	@JsonSerialize(using = StatusCampoConciliacaoJsonEnumSerializer.class)
	@JsonDeserialize(using = StatusCampoConciliacaoJsonEnumDesirializer.class)
	private StatusCampoConciliacaoJsonEnum status;
	
	public CampoConciliacaoJson(final CampoConciliacao model) {
		this.id = model.getId();
		this.nome = model.getCampo();
		this.valorCarga = model.getValorCarga();
		this.valorImportacao = model.getValorImportacao();
		this.valorConciliacao = model.getValor();
		this.status = StatusCampoConciliacaoJsonEnum.fromCodigo(model.getStatus().getCodigo());
		if (model.getCampoEquivalenteConciliacao() != null) {
			this.campoEquivalente = model.getCampoEquivalenteConciliacao().getCampoEquivalente();
			this.valorEquivalente = model.getCampoEquivalenteConciliacao().getValorEquivalente();
		}
	}
}
