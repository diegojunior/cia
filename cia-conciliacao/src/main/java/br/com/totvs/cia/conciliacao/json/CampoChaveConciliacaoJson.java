package br.com.totvs.cia.conciliacao.json;

import br.com.totvs.cia.conciliacao.model.CampoChaveConciliacao;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampoChaveConciliacaoJson implements Json {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String nome;

	private String valor;
	
	private String campoEquivalente;
	
	private String valorEquivalente;
	
	public CampoChaveConciliacaoJson(final CampoChaveConciliacao model) {
		this.id = model.getId();
		this.nome = model.getCampo();
		this.valor = model.getValor();
		if (model.getCampoEquivalenteConciliacao() != null) {
			this.campoEquivalente = model.getCampoEquivalenteConciliacao().getCampoEquivalente();
			this.valorEquivalente = model.getCampoEquivalenteConciliacao().getValorEquivalente();
		}
	}	
}