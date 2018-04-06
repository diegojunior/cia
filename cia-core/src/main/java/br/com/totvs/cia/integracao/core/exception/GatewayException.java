package br.com.totvs.cia.integracao.core.exception;

public class GatewayException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public GatewayException(String message) {
        super(message);
    }
	
	public GatewayException(final String message,
			final Throwable cause) {
		super(message, cause);
	}
	
}
