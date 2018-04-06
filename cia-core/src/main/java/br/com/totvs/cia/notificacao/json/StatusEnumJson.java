package br.com.totvs.cia.notificacao.json;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
@Getter
public enum StatusEnumJson implements PersistentEnum {
	
	INICIADO("Iniciado", "IN"), CONCLUIDO("Concluído", "CO"), ERRO("Erro", "ER");

	private final String nome;
	
	private final String codigo;


	private StatusEnumJson(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusEnumJson fromCodigo(final String codigo) {
		for (final StatusEnumJson tipo : StatusEnumJson.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	} 
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sb.append("Status: " + this.getNome() + " em : " + df.format(new Date()));
		return sb.toString();
	}
	
}
