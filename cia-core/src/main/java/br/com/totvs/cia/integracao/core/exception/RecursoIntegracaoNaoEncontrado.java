package br.com.totvs.cia.integracao.core.exception;

public class RecursoIntegracaoNaoEncontrado extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public RecursoIntegracaoNaoEncontrado() {
		super();
	}

	public RecursoIntegracaoNaoEncontrado(final String message) {
		super(message);
	}

	public RecursoIntegracaoNaoEncontrado(final Throwable cause) {
		super(cause);
	}

}
