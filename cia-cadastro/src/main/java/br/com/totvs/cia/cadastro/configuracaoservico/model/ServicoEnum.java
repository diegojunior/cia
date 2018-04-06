package br.com.totvs.cia.cadastro.configuracaoservico.model;

import br.com.totvs.cia.infra.enums.PersistentEnum;
import lombok.Getter;

@Getter
public enum ServicoEnum implements PersistentEnum {
	
	MOVIMENTO_RV_FUNDO_A_MERCADO("Movimento - Renda Variável - Fundo À Mercado", "MRVFM"),
	
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
	
	private final String nome;
	
	private final String codigo;

	private ServicoEnum(final String nome, final String codigo) {
		this.nome = nome;
		this.codigo = codigo;
	}
	
	public static ServicoEnum fromCodigo(final String codigo) {
		for (ServicoEnum servico : ServicoEnum.values()) {
			if (codigo.equalsIgnoreCase(servico.getCodigo()))
				return servico;
		}
		return null;
	}
	
	public Boolean isMovimentoRVFundoAMercado(){
		return this == MOVIMENTO_RV_FUNDO_A_MERCADO;
	}
	
	public Boolean isRendaVariavelAVista(){
		return this == RENDAVARIAVEL_AVISTA;
	}
	
	public Boolean isRendaVariavelOpcoes(){
		return this == RENDAVARIAVEL_OPCOES;
	}
	
	public Boolean isRendaVariavelEmprestimo(){
		return this == RENDAVARIAVEL_EMPRESTIMO;
	}
	
	public Boolean isRendaVariavelTermo(){
		return this == RENDAVARIAVEL_TERMO;
	}
	
	public Boolean isRendaVariavelFundoAMercado(){
		return this == RENDAVARIAVEL_FUNDO_A_MERCADO;
	}
	
	public Boolean isDerivativosDisponivel(){
		return this == DERIVATIVOS_DISPONIVEL;
	}
	
	public Boolean isDerivativosFuturos(){
		return this == DERIVATIVOS_FUTUROS;
	}
	
	public Boolean isRendaFixaCompromissada(){
		return this == RENDAFIXA_COMPROMISSADA;
	}
	
	public Boolean isRendaFixaDefinitiva(){
		return this == RENDAFIXA_DEFINITIVA;
	}
	
	public Boolean isRendaFixaTermo(){
		return this == RENDAFIXA_TERMO;
	}
	
	public Boolean isSwap(){
		return this == SWAP;
	}
	
	public Boolean isFundoInvestimento(){
		return this == FUNDO_INVESTIMENTO;
	}
	
	public Boolean isPatrimonio(){
		return this == PATRIMONIO;
	}
}
