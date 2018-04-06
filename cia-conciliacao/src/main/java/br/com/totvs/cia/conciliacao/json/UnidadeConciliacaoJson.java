package br.com.totvs.cia.conciliacao.json;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import br.com.totvs.cia.conciliacao.model.UnidadeConciliacao;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnidadeConciliacaoJson implements Json, Comparable<UnidadeConciliacaoJson> {
	 
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private List<CampoChaveConciliacaoJson> camposChave;
	
	private List<CampoConciliacaoJson> camposConciliaveis;
	
	private List<CampoInformativoJson> camposInformativos;
	
	@JsonProperty("status")
	@JsonSerialize(using = StatusUnidadeConciliacaoJsonEnumSerializer.class)
	@JsonDeserialize(using = StatusUnidadeConciliacaoJsonEnumDeserializer.class)
	private StatusUnidadeConciliacaoJsonEnum status;
	
	private String justificativa;
	
	public UnidadeConciliacaoJson(final UnidadeConciliacao model, final List<CampoChaveConciliacaoJson> chavesJson, 
			final List<CampoConciliacaoJson> conciliaveisJson, final List<CampoInformativoJson> informativosJson) {
		this.id = model.getId();
		this.camposChave = chavesJson;
		this.camposConciliaveis = conciliaveisJson;
		this.camposInformativos = informativosJson;
		this.justificativa = model.getJustificativa();
		this.status = model.getStatus() != null 
				? StatusUnidadeConciliacaoJsonEnum.fromCodigo(model.getStatus().getCodigo()) 
						: null;
	}
	
	@Override
	public int compareTo(final UnidadeConciliacaoJson outraUnidade) {
		int comparador = 0;
		for (CampoChaveConciliacaoJson campoChave : this.camposChave) {
			for (CampoChaveConciliacaoJson outroCampoChave : outraUnidade.getCamposChave()) {
				if (campoChave.getNome().equals(outroCampoChave.getNome())) {
					String valor = campoChave.getValor() != null ? campoChave.getValor() : "";
					String outroValor = outroCampoChave.getValor() != null ? outroCampoChave.getValor() : "";
					comparador = valor.compareTo(outroValor);
					break;
				}
			}
			if (comparador != 0) {
				return comparador;
			}
		}
		return comparador;
	}
}