package br.com.totvs.cia.infra.exception;

public class CiaBusinessException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CiaBusinessException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	public CiaBusinessException(final String message) {
		super(message);
	}

	public CiaBusinessException(final Throwable cause) {
		super(cause);
	}

}
