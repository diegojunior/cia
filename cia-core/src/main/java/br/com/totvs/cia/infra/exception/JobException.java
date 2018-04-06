package br.com.totvs.cia.infra.exception;

public class JobException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JobException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	public JobException(final String message) {
		super(message);
	}

	public JobException(final Throwable cause) {
		super(cause);
	}

}
