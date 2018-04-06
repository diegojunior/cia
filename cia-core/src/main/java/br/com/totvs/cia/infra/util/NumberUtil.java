package br.com.totvs.cia.infra.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.google.common.base.Strings;

public class NumberUtil {
	
	private static final Logger log = Logger.getLogger(NumberUtil.class);

	public static String removeThousandsSeparation(final String number) {
		if (number.indexOf(",") > 0) {
			return number.replaceAll(Pattern.quote("."), "");
		}
		return number;
	}
	
	public static String replaceDotToCommaAndFormat(final Double number) {
		if (number != null) {
			String value = new BigDecimal(number.toString()).toPlainString();
			if (value.indexOf(".") > 0) {
				String valorFormatado = value.replaceAll(Pattern.quote("."), ",");
				Boolean isPossuiSomenteUmaCasaDecimal = valorFormatado.split(Pattern.quote(","))[1].length() == 1;
				if (isPossuiSomenteUmaCasaDecimal) {
					return valorFormatado + "0";
				}
				return valorFormatado;
			} else if (value.indexOf(",") == -1) {
				return value + ",00";
			}
		}
		return null;
	}

	public static BigDecimal obtemValor(final String valor, final String pattern) {
		final StringBuilder sb = new StringBuilder();
		final boolean valorNegativo = verificaSeValorNegativo(valor);
		String novoValor = valorNegativo ? excluiSinalNegativo(valor) : valor;
		
		final String[] gruposPattern = pattern.split("[.|,]");
		final String[] gruposValores = novoValor.split("[.|,]");
		int qtdGruposPattern = gruposPattern.length;
		int qtdGruposValores = gruposValores.length;
		
		if (isDecimal(qtdGruposPattern)) {
			int qtdCasasDecimaisPattern = gruposPattern[--qtdGruposPattern].length();
			int qtdCasasInteiroPattern = getQuantidadeCasasInteiras(gruposPattern);
			
			if (isDecimal(qtdGruposValores)) {
				String valorDecimal = getValorDecimal(gruposValores);
				String valorInteiro = getValorInteiro(gruposValores);
				valorDecimal = Strings.padEnd(valorDecimal, qtdCasasDecimaisPattern, '0');
				valorInteiro = Strings.padStart(valorInteiro, qtdCasasInteiroPattern, '0');
				sb.append(valorInteiro).append(".").append(valorDecimal);
			} else {
				if (novoValor.length() < qtdCasasDecimaisPattern) {
					novoValor = Strings.padStart(novoValor, qtdCasasDecimaisPattern, '0');
				}
				int posicaoCorte = novoValor.length() - qtdCasasDecimaisPattern;
				String valorInteiro = novoValor.substring(0, posicaoCorte);
				String valorDecimal = novoValor.substring(posicaoCorte);
				sb.append(valorInteiro).append(".").append(valorDecimal);
			}
			
		} else {
			sb.append(novoValor);
		}
		
		try {
			BigDecimal valorBigdecimal = new BigDecimal(sb.toString());
			
			if (valorNegativo) {
				return valorBigdecimal.negate();
			}
			
			return valorBigdecimal;
		} catch (final NumberFormatException e) {
			log.error("Erro ao efetuar o parser do valor informado ( " + valor + " )", e);
			throw e;
		}
	}
	
	private static Boolean verificaSeValorNegativo(final String valor) {
		try {
			boolean valorNegativo = false;
			if (valor.contains("-")) {
				valorNegativo = true;
			}
			return valorNegativo;
		} catch (Exception ex) {
			log.error("Não foi possível verificar se o valor é negativo. Favor verificar o Layout e o Conteúdo do Arquivo.", ex);
			throw new IllegalArgumentException("Ocorreu erro na verificação de valores negativos. Favor Analise o Layout e o Conteúdo do Arquivo.", ex);
		}
	}
	
	private static String excluiSinalNegativo(final String valor) {
		return valor.replace("-", "0");
	}
	
	private static boolean isDecimal(final int qtdGrupos) {
		return qtdGrupos > 1;
	}
	
	private static int getQuantidadeCasasInteiras(final String[] gruposPattern) {
		int total = 0;
		for (int x = 0; x < gruposPattern.length-1; x++) {
			total += gruposPattern[x].length();
		}
		return total;
	}
	
	private static String getValorDecimal(final String[] gruposValores) {
		int length = gruposValores.length;
		return gruposValores[--length];
		
	}
	
	private static String getValorInteiro(final String[] gruposValores) {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < gruposValores.length-1; x++) {
			sb.append(gruposValores[x]);
		}
		return sb.toString();
	}
}