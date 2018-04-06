package br.com.totvs.cia.parametrizacao.infra.exception;

import br.com.totvs.cia.infra.exception.CiaBusinessException;


public class ChaveNaoEncontradaException extends CiaBusinessException {

	private static final long serialVersionUID = 1L;

	public ChaveNaoEncontradaException(String message) {
		super(message);
	}


}
