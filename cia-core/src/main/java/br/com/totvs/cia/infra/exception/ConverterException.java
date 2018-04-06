package br.com.totvs.cia.infra.exception;

public class ConverterException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public ConverterException(Throwable cause) {
        super(cause);
    }
	
	public ConverterException(String message) {
        super(message);
    }
	
	public ConverterException(String message, Throwable cause) {
        super(message, cause);
    }
}