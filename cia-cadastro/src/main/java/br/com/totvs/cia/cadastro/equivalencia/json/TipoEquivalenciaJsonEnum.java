package br.com.totvs.cia.cadastro.equivalencia.json;

import br.com.totvs.cia.infra.json.Json;
import lombok.Getter;

@Getter
public enum TipoEquivalenciaJsonEnum implements Json {
	
	CORRETORA("Corretora", "23");

	private final String nome;
	
	private final String codigo;


	private TipoEquivalenciaJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static TipoEquivalenciaJsonEnum fromCodigo(final String codigo) {
		for (final TipoEquivalenciaJsonEnum tipo : TipoEquivalenciaJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	} 
	
}
