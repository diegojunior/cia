package br.com.totvs.cia.conciliacao.json;

import br.com.totvs.cia.conciliacao.model.CampoInformativo;
import br.com.totvs.cia.infra.json.Json;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampoInformativoJson implements Json {

	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String nome;

	private String valor;
	
	public CampoInformativoJson(final CampoInformativo model) {
		this.id = model.getId();
		this.nome = model.getCampo();
		this.valor = model.getValor();
	}
}