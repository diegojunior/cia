package br.com.totvs.cia.parametrizacao.perfilconciliacao.json;

import java.util.HashMap;
import java.util.Map;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;
import lombok.Setter;

public enum LocalizacaoCamposJsonEnum implements PersistentEnum {
	
	SESSAO("Sessão do Layout", "SES"),
	UNIDADE("Parametrização de Unidade", "UNI");
	
	@Getter
	@Setter
	private String nome;
	
	@Getter
	@Setter
	private String codigo;
	
	private static final Map<String, LocalizacaoCamposJsonEnum> lookup = new HashMap<String, LocalizacaoCamposJsonEnum>();
	
	static {
		for (LocalizacaoCamposJsonEnum tipo : LocalizacaoCamposJsonEnum.values()) {
			lookup.put(tipo.getCodigo(), tipo);
		}
	}

	private LocalizacaoCamposJsonEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
		
	}
	
	public static LocalizacaoCamposJsonEnum fromCodigo(final String codigo) {
		for (LocalizacaoCamposJsonEnum tipo : LocalizacaoCamposJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo())) 
				return tipo;
		}
		return null;
	}
	
	public static LocalizacaoCamposJsonEnum get(final String codigo) {
		return lookup.get(codigo);
	}
	
	public Boolean isSessao() {
		return this == SESSAO;
	}
	
	public Boolean isUnidade() {
		return this == UNIDADE;
	}
	
}
