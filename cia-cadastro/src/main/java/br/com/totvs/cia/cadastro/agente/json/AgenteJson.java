package br.com.totvs.cia.cadastro.agente.json;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import br.com.totvs.cia.cadastro.agente.model.Agente;
import br.com.totvs.cia.infra.json.Json;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgenteJson implements Json {
	
	private static final long serialVersionUID = 1L;
	
	private String id;
	
	private String idLegado;
	
	private String codigo;
	
	private String descricao;
	
	private String codigoClearing;
	
	public AgenteJson(final String idLegado, final String codigo, final String descricao,
			final String codigoClearing) {
		this.idLegado = idLegado;
		this.codigo = codigo;
		this.descricao = descricao;
		this.codigoClearing = codigoClearing;
	}

	public AgenteJson(final Agente model) {
		this.id = model.getId();
		this.idLegado = model.getIdLegado();
		this.codigo = model.getCodigo();
		this.descricao = model.getDescricao();
		this.codigoClearing = model.getCodigoClearing();
	}
}
