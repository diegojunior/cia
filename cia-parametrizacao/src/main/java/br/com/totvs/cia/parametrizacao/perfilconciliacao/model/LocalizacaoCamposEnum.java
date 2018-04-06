package br.com.totvs.cia.parametrizacao.perfilconciliacao.model;

import lombok.Getter;
import br.com.totvs.cia.infra.enums.PersistentEnum;

public enum LocalizacaoCamposEnum implements PersistentEnum {

	SESSAO("Sessão do Layout", "SES"), 
	UNIDADE("Parametrização de Unidade", "UNI");
	
	@Getter
	private final String nome;
	@Getter
	private final String codigo;

	private LocalizacaoCamposEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
		
	}
	
	public static LocalizacaoCamposEnum fromCodigo(final String codigo) {
		for (LocalizacaoCamposEnum tipo : LocalizacaoCamposEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}
	
}
