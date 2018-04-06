package br.com.totvs.cia.parametrizacao.dominio.exception;

import br.com.totvs.cia.infra.exception.CiaBusinessException;

public class EquivalenciaDuplicadaException extends CiaBusinessException {

	private static final long serialVersionUID = 1L;

	public EquivalenciaDuplicadaException(final String message,
			final Throwable cause) {
		super(message, cause);
	}

	public EquivalenciaDuplicadaException(final String message) {
		super(message);
	}

	public EquivalenciaDuplicadaException(final Throwable cause) {
		super(cause);
	}

}
