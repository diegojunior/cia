package br.com.totvs.cia.infra.exception;

public class NumberParserException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NumberParserException() {
		super();
	}

	public NumberParserException(String message, Throwable cause) {
		super(message, cause);
	}

	public NumberParserException(String message) {
		super(message);
	}

	public NumberParserException(Throwable cause) {
		super(cause);
	}

}
