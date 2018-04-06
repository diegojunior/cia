package br.com.totvs.cia.notificacao.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
@Getter
public enum StatusEnum implements PersistentEnum {
	
	INICIADO("Iniciado", "IN"), 
	
	ERRO("Erro", "ER"),
	
	CONCLUIDO("Concluído", "CO");
	

	private final String nome;
	
	private final String codigo;


	private StatusEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}

	public static StatusEnum fromCodigo(final String codigo) {
		for (final StatusEnum tipo : StatusEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		final SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		sb.append(this.getNome() + " em : " + df.format(new Date()));
		sb.append(" - ");
		return sb.toString();
	}
	
}
