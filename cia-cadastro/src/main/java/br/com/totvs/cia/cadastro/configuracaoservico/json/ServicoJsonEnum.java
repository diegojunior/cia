package br.com.totvs.cia.cadastro.configuracaoservico.json;

import br.com.totvs.cia.infra.json.Json;
import lombok.Getter;

@Getter
public enum ServicoJsonEnum  implements Json {

	RENDAVARIAVEL_AVISTA("Renda Variável À Vista", "RVA"),
	RENDAVARIAVEL_OPCOES("Renda Variável Opcões", "RVO"),
	RENDAVARIAVEL_EMPRESTIMO("Renda Variável Emprestimo", "RVE"),
	RENDAVARIAVEL_TERMO("Renda Variável Termo", "RVT"),
	RENDAVARIAVEL_FUNDO_A_MERCADO("Renda Variável Fundo À Mercado", "RVF"),
	
	DERIVATIVOS_DISPONIVEL("Derivativos Disponível", "DED"),
	DERIVATIVOS_FUTUROS("Derivativos Futuros", "DEF"),
	
	RENDAFIXA_COMPROMISSADA("Renda Fixa Compromissada", "RFC"),
	RENDAFIXA_DEFINITIVA("Renda Fixa Definitiva", "RFD"),
	RENDAFIXA_TERMO("Renda Fixa Termo", "RFT"),
	
	SWAP("Swap", "SWP"),
	
	FUNDO_INVESTIMENTO("Fundo Investimento", "FUI"),
	
	PATRIMONIO("Patrimonio", "PTR");

	private final String codigo;
	private final String nome;

	private ServicoJsonEnum(final String nome, final String codigo) {
		this.codigo = codigo;
		this.nome = nome;
	}

	public static ServicoJsonEnum fromCodigo(final String codigo) {
		for (ServicoJsonEnum tipo : ServicoJsonEnum.values()) {
			if (codigo.equalsIgnoreCase(tipo.getCodigo()))
				return tipo;
		}
		return null;
	}
	
}
