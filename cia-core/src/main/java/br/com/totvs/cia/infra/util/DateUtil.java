package br.com.totvs.cia.infra.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateUtil {
	
	private static final Logger log = Logger.getLogger(DateUtil.class);
	
	public static final String yyyy_MM_dd = "yyyy-MM-dd";
	public static final String yyyyMMdd = "yyyyMMdd";
	public static final String ddMMyyyy = "ddMMyyyy";
	public static final String yyyyMMddHHmmssSSSZ = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	public static final String ddMMyyyyHHmmss = "dd/MM/yyyy HH:mm:ss";
	
	public static String format(final Date date, final String pattern){
		if (date != null && pattern != null){
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			sdf.setLenient(false);
			return sdf.format(date);
		}
		return null;
	}
	
	public static Date parse(final String date, final String pattern) throws ParseException{
		if (date != null && pattern != null){
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			sdf.setLenient(false);
			return sdf.parse(date);
		}
		return null;
	}
	
	public static String transformToYYYYMMDD(final String data) {
		// YYYY-MM-DD
		if (data.matches("\\d{4}-\\d{2}-\\d{2}")) {
			return data.replaceAll("-", "");
		}
		// DD-MM-YYYY
		if (data.matches("\\d{2}-\\d{2}-\\d{4}")) {
			String ano = data.substring(data.length() - 4, data.length());
			String mes = data.substring(data.length() - 7, data.length()-5);
			String dia = data.substring(0, 2); 
			return ano+mes+dia;
		}
		// YYYY/MM/DD
		if (data.matches("\\d{4}/\\d{2}/\\d{2}")) {
			return data.replaceAll("/", "");
		}
		// DD/MM/YYYY
		if (data.matches("\\d{2}/\\d{2}/\\d{4}")) {
			String ano = data.substring(data.length() - 4, data.length());
			String mes = data.substring(data.length() - 7, data.length()-5);
			String dia = data.substring(0, 2); 
			return ano+mes+dia;
		}
		// DDMMYYYY ou YYYYMMDD
		if (data.matches("\\d{8}")) {
			String ano = data.substring(data.length() - 4, data.length());
			String mes = data.substring(data.length() - 6, data.length() - 4);
			String dia = data.substring(0, 2);
			if (testaDataValida(ano + mes + dia, yyyyMMdd)) {
				return ano + mes + dia;
			}
			ano = data.substring(0, 4);
			mes = data.substring(data.length() - 4, data.length() - 2);
			dia = data.substring(data.length() - 2, data.length());
			testaDataValida(dia + mes + ano, ddMMyyyy);
			return ano + mes + dia;
		}
		
		return data;
	}

	private static boolean testaDataValida(final String valor, 
			final String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			sdf.setLenient(false);
			sdf.parse(valor);
		} catch (ParseException e) {
			log.debug("Tentando converter o valor no formato data.", e);
			return false;
		}
		return true;
	}
	
}