package br.com.totvs.cia.conciliacao.model;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.apache.log4j.Logger;

import br.com.totvs.cia.cadastro.tipo.TipoValorDominioEnum;
import br.com.totvs.cia.importacao.model.CampoImportacao;
import br.com.totvs.cia.infra.util.DateUtil;
import br.com.totvs.cia.infra.util.NumberUtil;

public class FormatadorConciliacao {
	
	private static final Logger log = Logger.getLogger(TipoValorDominioEnum.class);
	
	public static String format(final CampoImportacao campo) {
		try {
			if (campo.getCampo().isEfetuaCalculo()) {
				return NumberUtil.obtemValor(campo.getValor(), campo.getPattern()).toString();
			} else if (campo.getCampo().getTipo().isDate()) {
				Date dataConvertida = DateUtil.parse(campo.getValor(), campo.getPattern());
				return DateUtil.format(dataConvertida, DateUtil.yyyyMMdd);
			}
			
		} catch (NumberFormatException | ParseException e) {
			log.error("O arquivo importado possui Formato diferente para o campo  " + "\"" + campo.getCampo().getCodigo() + "\"");
			throw new IllegalArgumentException("O arquivo importado possui Formato diferente para o campo  " + "\"" + campo.getCampo().getCodigo() + "\"");
		}
		return campo.getValor();
	}
	
	public static boolean datasIguais(final Date d1, final Date d2) throws ParseException {
		return d1.equals(d2);
	}
	
	public static BigDecimal subtrair(final BigDecimal valorLeft, final BigDecimal valorRight) {
		return valorLeft.subtract(valorRight);
			
	}
}